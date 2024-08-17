package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.boundaries.routes.RouteResponse;
import org.example.boundaries.stops.StopsRequest;
import org.example.dal.LineStopRepository;
import org.example.data.LineStopEntity;
import org.example.dto.transit.Station;
import org.example.dto.transit.TransitDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.example.utils.ConvertEntityToDto.convertLineStopToStation;


@Service
@Slf4j
@RequiredArgsConstructor
public class RoutesServiceImpl implements RoutesService {
    private final WebClient.Builder webClientBuilder;
    private final LineStopRepository lineStopCrud;
    private final LineStopRepository lineStopRepository;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Flux<RouteResponse> getRoutesWithIntermediateStations(StopsRequest stopsRequest) {
        return fetchRoutes(stopsRequest)
                .flatMap(response -> {
                    // Convert the transit details from Map<String, Object> to List<TransitDetails>
                    List<TransitDetails> transitDetails = objectMapper.convertValue(
                            response.getRouteFlow().get("transitDetails"),
                            new TypeReference<List<TransitDetails>>() {}
                    );

                    return Flux.fromIterable(transitDetails)
                            .flatMap(transitDetail -> {
                                String lineNumber = transitDetail.getLineNumber();
                                String originStation = transitDetail.getStopDetails().getDepartureStop().getName();
                                String destinationStation = transitDetail.getStopDetails().getArrivalStop().getName();

                                // Get all intermediate stations of a transit and convert them into a list of dto.Station
                                Mono<List<Station>> intermediateStationsMono = convertLineStopToStation(
                                        getStationsBetweenInTransit(lineNumber, originStation, destinationStation)
                                ).collectList();

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


    @Override
    public Flux<RouteResponse> fetchRoutes(StopsRequest stopsRequest) {
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

    private Flux<LineStopEntity> getStationsBetweenInTransit(String lineNumber, String origin, String destination) {
        String finalLineNumber = "89";
        origin = "שדרות קוגל/הלוחמים";
        destination = "דרך נמיר/יהודה המכבי";
        Mono<LineStopEntity> originMono =
                lineStopCrud.findByLineNumberAndStopName(finalLineNumber, origin)
                .switchIfEmpty(Mono.error(new RuntimeException("Origin stop not found")));

        Mono<LineStopEntity> destinationMono =
                lineStopCrud.findByLineNumberAndStopName(finalLineNumber, destination)
                .switchIfEmpty(Mono.error(new RuntimeException("Destination stop not found")));

        // Combine origin and destination stops, then fetch stops between them
        return Mono.zip(originMono, destinationMono)
                .flatMapMany(tuple -> {
                    LineStopEntity originStop = tuple.getT1();
                    LineStopEntity destinationStop = tuple.getT2();

                    // Fetch stops between origin and destination stop names
                    Flux<LineStopEntity> stops = lineStopRepository.findLineStopEntitiesByLineNumber(finalLineNumber)
                            .collectList()
                            .flatMapMany(stopsList -> {

                                // If origin is found and there are enough stations
                                if (!stopsList.isEmpty()) {
                                    List<LineStopEntity> result = stopsList.subList(originStop.getStopOrder() + 1, destinationStop.getStopOrder() - 1);
                                    return Flux.fromIterable(result);
                                }

                                // Return an empty Flux if the origin stop is not found
                                return Flux.empty();
                            });

                    return stops;
                });
    }
}
