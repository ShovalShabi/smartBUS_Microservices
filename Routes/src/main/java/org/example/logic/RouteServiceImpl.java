package org.example.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.boundary.RouteRequest;
import org.example.boundary.RouteResponse;
import org.example.dto.polyline.LatLng;
import org.example.dto.transit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.example.utils.TimeMeasurement.getFinalArrivalTime;
import static org.example.utils.TimeMeasurement.getInitialDepartureTime;
import static org.example.utils.ZipMonoOfLists.zipMonoListOfLists;

@Service
@Slf4j
public class RouteServiceImpl implements RouteService {
    private final TransitService transitService;
    private final PolylineService polylineService;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @Autowired
    public RouteServiceImpl(TransitService transitService, PolylineService polylineService) {
        this.transitService = transitService;
        this.polylineService = polylineService;
    }

    /**
     * Get the route for the given route request
     * @param routeRequest The route request to get the route for
     * @param isSorted Whether to sort the route responses by departure time
     * @return A Flux of route responses
     */
    @Override
    public Flux<RouteResponse> getRoute(RouteRequest routeRequest, Boolean isSorted) {
        // Get the transit details and polylines
        // Convert the tuple of transit details and polylines to a list of route responses
        return getTransitDetailsAndPolylines(routeRequest)
                .flatMapMany(tuple -> tupleToListOfRouteResponses(tuple, routeRequest))
                .transformDeferred(responses -> isSorted ? sortedResponses(responses) : responses);
    }

    /**
     * Convert the tuple of transit details and polylines to a list of route responses
     * @param tuple The tuple of transit details and polylines
     * @param routeRequest The route request to get the route responses for
     * @return A list of route responses
     */
    private Flux<RouteResponse> tupleToListOfRouteResponses(Tuple2<Mono<List<List<TransitDetails>>>, Mono<List<List<LatLng>>>> tuple,
                                                            RouteRequest routeRequest) {
        List<RouteResponse> routeResponses = new ArrayList<>();
        var transitDetailsMono = tuple.getT1();
        var latLngMono = tuple.getT2();
        var zippedMono = zipMonoListOfLists(transitDetailsMono, latLngMono);
        return zippedMono.flatMapMany(zippedLists ->
                Flux.fromIterable(zippedLists)
                        .map(innerTuple -> {
                            List<TransitDetails> detailsList = innerTuple.getT1();
                            List<LatLng> polylineList = innerTuple.getT2();
                            return RouteResponse.builder()
                                    .origin(routeRequest.getOriginAddress())
                                    .destination(routeRequest.getDestinationAddress())
                                    .publishedTimestamp(LocalDateTime.now().toString())
                                    .initialDepartureTime(getInitialDepartureTime(detailsList))
                                    .finalArrivalTime(getFinalArrivalTime(detailsList))
                                    .routeFlow(Map.of("transitDetails", detailsList, "polyline", polylineList))
                                    .build();
                        })
        );
    }

    /**
     * Construct the details map for the response
     * @param groupedTransitDetails The grouped transit details to construct the map from
     * @return A Mono of the constructed details map
     */
    private Mono<List<Map<String, Object>>> createRouteDetails(List<TransitDetails> groupedTransitDetails) {
        try {
            // Construct the response
            List<Map<String, Object>> routeDetailsFlow = new ArrayList<>();

            // Add the needed details to the response list for each transit detail
            for (TransitDetails transitDetails : groupedTransitDetails) {
                Map<String, Object> neededDetails = new LinkedHashMap<>();
                neededDetails.put("arrivalTime", transitDetails.getLocalizedValues().getArrivalTime().getTime().getText());
                neededDetails.put("departureTime", transitDetails.getLocalizedValues().getDepartureTime().getTime().getText());
                neededDetails.put("agency", transitDetails.getTransitLine().getAgencies().get(0).getName());
                neededDetails.put("lineNumber", transitDetails.getTransitLine().getNameShort());
                neededDetails.put("stopCounts", transitDetails.getStopCount());
                neededDetails.put("stopDetails", transitDetails.getStopDetails());

                routeDetailsFlow.add(neededDetails);
            }

            return Mono.just(routeDetailsFlow);
        } catch (Exception e) {
            log.error("Error creating route details", e);
            return Mono.error(e); // Propagate the exception
        }
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
                        String departureTime = routeResponse.getInitialDepartureTime();
                        return LocalDateTime.parse(departureTime, formatter);
                    } catch (Exception e) {
                        log.error("Error parsing departure time for route response: {}", routeResponse, e);
                        return LocalDateTime.MIN; // Provide a default value for sorting if parsing fails
                    }
                }))
                .doOnNext(routeResponse -> log.info("Sorted route response: {}", routeResponse));
    }

    /**
     * Create the route details from the grouped transit details
     * @param routeRequest The route request to get the route details for
     * @return A Mono Tuple of the transit details and polylines
     */
    private Mono<Tuple2<Mono<List<List<TransitDetails>>>, Mono<List<List<LatLng>>>>> getTransitDetailsAndPolylines(RouteRequest routeRequest) {
        var transitDetailsMono = transitService.getTransitDetails(routeRequest).cache();
        var polylinesMono = polylineService.getPolyline(routeRequest).cache();
        return transitDetailsMono.zipWith(polylinesMono, (transit, polylines) -> Tuples.of(Mono.just(transit), Mono.just(polylines)));
    }
}
