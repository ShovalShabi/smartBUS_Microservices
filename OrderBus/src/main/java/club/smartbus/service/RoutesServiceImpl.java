package club.smartbus.service;

import club.smartbus.boundaries.routes.RouteResponse;
import club.smartbus.boundaries.stops.StopsRequest;
import club.smartbus.dto.transit.Station;
import club.smartbus.dto.transit.TransitDetails;
import club.smartbus.utils.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutesServiceImpl implements RoutesService {
    private final WebClient.Builder webClientBuilder;
    private final BusService busService;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retrieves routes and includes intermediate stations between the origin and destination for each transit detail.
     *
     * <p>This method fetches route details and then processes each transit detail to retrieve the intermediate
     * stations between the specified origin and destination stations. The intermediate stations are then added
     * to the corresponding transit details.</p>
     *
     * <p>The process includes:</p>
     * <ul>
     *   <li>Fetching route details using the {@link #fetchRoutes(StopsRequest)} method.</li>
     *   <li>Extracting the transit details from the response and converting them from a {@code Map<String, Object>}
     *       to a {@code List<TransitDetails>}.</li>
     *   <li>For each transit detail, retrieving the intermediate stations between the origin and destination using
     *       the {@link #getStationsBetweenInTransit(String, String, String)} method.</li>
     *   <li>Updating the transit details with the list of intermediate stations.</li>
     *   <li>Returning the updated route response.</li>
     * </ul>
     *
     * @param stopsRequest the request containing information about the stops for which routes need to be fetched
     * @return a {@link Flux<  RouteResponse  >} containing the route details with intermediate stations included
     */
    @Override
    public Flux<RouteResponse> getRoutesWithIntermediateStations(StopsRequest stopsRequest) {
        return fetchRoutes(stopsRequest)
                .flatMap(response -> {
                    // Convert the transit details from Map<String, Object> to List<TransitDetails>
                    List<TransitDetails> transitDetails = objectMapper.convertValue(
                            response.getRouteFlow().get("transitDetails"),
                            new TypeReference<List<TransitDetails>>() {
                            }
                    );

                    return Flux.fromIterable(transitDetails)
                            .flatMap(transitDetail -> {
                                String lineNumber = transitDetail.getLineNumber();
                                String originStation = transitDetail.getStopDetails().getDepartureStop().getName();
                                String destinationStation = transitDetail.getStopDetails().getArrivalStop().getName();

                                // Get all intermediate stations of a transit and convert them into a list of dto.Station
                                Mono<List<Station>> intermediateStationsMono =
                                        getStationsBetweenInTransit(lineNumber, originStation, destinationStation)
                                                .collectList();

                                // Update the stop details with the list of stations
                                return intermediateStationsMono.map(intermediateStations -> {
                                    transitDetail.getStopDetails().setStations(Optional.of(intermediateStations));
                                    return transitDetail;
                                });
                            })
                            .collectList()
                            .map(updatedTransitDetails -> {
                                response.getRouteFlow().put("transitDetails", updatedTransitDetails);
                                return response;
                            });
                });
    }

    /**
     * Fetches routes based on the provided stops request.
     *
     * <p>This method sends a POST request to an external service to fetch route details based on the given
     * stops request. The response is then processed into a {@link Flux<RouteResponse>}.</p>
     *
     * <p>Logging is performed to track the start and completion of the request, as well as any errors that
     * might occur during the request.</p>
     *
     * @param stopsRequest the request containing the route information to be fetched
     * @return a {@link Flux<RouteResponse>} containing the route details
     */
    private Flux<RouteResponse> fetchRoutes(StopsRequest stopsRequest) {
        log.info("Fetching routes for stops: {}", stopsRequest);

        try {
            return webClientBuilder.build()
                    .post()
                    .bodyValue(stopsRequest.getRouteRequest())
                    .retrieve()
                    .bodyToFlux(RouteResponse.class)
                    .doOnError(error -> log.error("Error fetching routes: {}", error.getMessage()))
                    .doOnComplete(() -> log.info("Route fetching completed"));
        } catch (Exception e) {
            log.error("Exception occurred while fetching routes: {}", e.getMessage());
            return Flux.error(e); // Return error Flux if an exception occurs
        }
    }

    /**
     * Retrieves intermediate stations between the origin and destination for a given bus line.
     *
     * <p>This method leverages the {@link BusService#getBusLineStations(String, String, String, int, int)} method
     * to fetch stations between the specified origin and destination stations for a given bus line. The method
     * also applies default pagination settings.</p>
     *
     * @param lineNumber  the line number of the bus route
     * @param origin      the name of the origin station
     * @param destination the name of the destination station
     * @return a {@link Flux<Station>} containing the stations between the origin and destination stations
     */
    private Flux<Station> getStationsBetweenInTransit(String lineNumber, String origin, String destination) {
        return busService.getBusLineStations(lineNumber, origin, destination, Integer.parseInt(Constants.DEFAULT_PAGE_SIZE), Integer.parseInt(Constants.DEFAULT_PAGE));
    }
}
