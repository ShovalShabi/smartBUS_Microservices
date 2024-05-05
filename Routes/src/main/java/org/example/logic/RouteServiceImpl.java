package org.example.logic;

import lombok.extern.slf4j.Slf4j;
import org.example.boundary.RouteRequest;
import org.example.boundary.RouteResponse;
import org.example.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class RouteServiceImpl implements RouteService {
    private final GoogleApiService googleApiService;

    @Autowired
    public RouteServiceImpl(GoogleApiService googleApiService) {
        this.googleApiService = googleApiService;
    }

    /**
     * Get the route from the Google API
     * @param routeRequest The route request to get the route for
     * @param isSorted Whether the routes should be sorted
     * @return A Flux of the route responses
     */
    @Override
    public Flux<RouteResponse> getRoute(RouteRequest routeRequest, Boolean isSorted) {
        // Get the routes from the Google API
        // Converting API Json to needed Boundary Object
        Flux<RouteResponse> responses =
                // Group the transit details by list of same Route
                groupTransitDetailsByList(googleApiService.getRouteFromApi(routeRequest))
                .flatMapMany(Flux::fromIterable)
                 // Get the route responses from the grouped transit details
                .flatMap(groupedTransitDetails -> getRouteResponse(groupedTransitDetails, routeRequest))
                .doOnError(error -> log.error("Error getting routes", error))
                .onErrorResume(error -> Flux.empty()); // Provide a fallback value in case of error

        // Sort the responses if needed and return them
        return isSorted ? sortedResponses(responses) : responses;
    }

    /**
     * Get the route responses from the grouped transit details
     * @param groupedTransitDetails The grouped transit details to get the route responses from
     * @return A Mono of the route responses
     */
    private Mono<RouteResponse> getRouteResponse(List<TransitDetails> groupedTransitDetails, RouteRequest routeRequest) {
        return createRouteDetails(groupedTransitDetails)
                .mapNotNull(routeDetails -> {
                    try {
                        return RouteResponse.builder()
                                .routeFlow(routeDetails)
                                .origin(routeRequest.getOriginAddress())
                                .destination(routeRequest.getDestinationAddress())
                                .publishedTimestamp(String.valueOf(LocalDateTime.now()))
                                .build();
                    } catch (Exception e) {
                        log.error("Error creating route response", e);
                        return null; // Provide a default value in case of error
                    }
                })
                .doOnError(error -> log.error("Failed to create route response", error))
                .onErrorResume(error -> Mono.error(new Exception("Failed to create route response", error)));
    }

    /**
     * Get the steps from the legs and combine them into a single list
     * @param routesMono The routes to get the steps from
     * @return A Mono of the combined steps
     */
    private Mono<List<List<TransitDetails>>> groupTransitDetailsByList(Mono<Routes> routesMono) {
        // Get the legs from the routes
        Mono<List<Leg>> legsMono = combineRouteList(routesMono);

        // Get the transit details from the steps and group them by leg
        return legsMono.flatMap(legs -> {
                    List<List<TransitDetails>> groupedTransitDetails = new ArrayList<>();
                    try {
                        for (Leg leg : legs) {
                            List<TransitDetails> legTransitDetails =
                                    leg.getSteps().stream()
                                            .map(Step::getTransitDetails) // Get transit details for each step
                                            .filter(Objects::nonNull) // Filter out null transit details lists
                                            .toList(); // Collect into a list
                            // Add the list of transit details to the grouped list
                            groupedTransitDetails.add(legTransitDetails);
                        }
                        // Return the grouped transit details
                        return Mono.just(groupedTransitDetails);
                    } catch (Exception e) {
                        log.error("Error grouping transit details by list", e);
                        return Mono.error(e); // Propagate the exception
                    }
                })
                .doOnError(error -> log.error("Failed to group transit details by list", error))
                .onErrorResume(error -> Mono.just(List.of())); // Provide a fallback value in case of error
    }

    /**
     * Construct the details map for the response
     * @param groupedTransitDetails The grouped transit details to construct the map from
     * @return A Mono of the constructed details map
     */
    private Mono<List<Map<String, Object>>> createRouteDetails(List<TransitDetails> groupedTransitDetails) {
        try {
            // Construct the response
            List<Map<String, Object>> routeFlow = new ArrayList<>();

            // Add the needed details to the response list for each transit detail
            for (TransitDetails transitDetails : groupedTransitDetails) {
                Map<String, Object> neededDetails = new LinkedHashMap<>();
                neededDetails.put("arrivalTime", transitDetails.getLocalizedValues().getArrivalTime().getTime().getText());
                neededDetails.put("departureTime", transitDetails.getLocalizedValues().getDepartureTime().getTime().getText());
                neededDetails.put("agency", transitDetails.getTransitLine().getAgencies().get(0).getName());
                neededDetails.put("lineNumber", transitDetails.getTransitLine().getNameShort());
                neededDetails.put("stopCounts", transitDetails.getStopCount());
                neededDetails.put("stopDetails", transitDetails.getStopDetails());

                routeFlow.add(neededDetails);
            }

            return Mono.just(routeFlow);
        } catch (Exception e) {
            log.error("Error creating route details", e);
            return Mono.error(e); // Propagate the exception
        }
    }

    /**
     * Get the legs from the routes and combine them into a single list
     * @param routesMono The routes to get the legs from
     * @return A Mono of the combined legs
     */
    private Mono<List<Leg>> combineRouteList(Mono<Routes> routesMono) {
        return routesMono.flatMap(routes -> {
                    try {
                        // Get the legs from the routes as a Flux
                        Flux<Leg> legsFlux = Flux.fromIterable(routes.getRoutes())
                                .flatMapIterable(Route::getLegs); // Flatten the list of legs

                        // Collect all legs into a single list
                        return legsFlux.collectList();
                    } catch (Exception e) {
                        log.error("Error combining route list", e);
                        return Mono.error(e); // Propagate the exception
                    }
                })
                .doOnError(error -> log.error("Failed to combine route list", error))
                .onErrorResume(error -> Mono.just(List.of())); // Provide a fallback value in case of error
    }

    /**
     * Sort the route responses by departure time
     * @param routeResponses The route responses to sort
     * @return A Flux of the sorted route responses
     */
    private Flux<RouteResponse> sortedResponses(Flux<RouteResponse> routeResponses) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return routeResponses
                .sort(Comparator.comparing(routeResponse -> {
                    try {
                        String departureTime = routeResponse.getRouteFlow().get(0).get("departureTime").toString();
                        return LocalDateTime.parse(departureTime, formatter);
                    } catch (Exception e) {
                        log.error("Error parsing departure time for route response: {}", routeResponse, e);
                        return LocalDateTime.MIN; // Provide a default value for sorting if parsing fails
                    }
                }))
                .doOnNext(routeResponse -> log.info("Sorted route response: {}", routeResponse));
    }
}
