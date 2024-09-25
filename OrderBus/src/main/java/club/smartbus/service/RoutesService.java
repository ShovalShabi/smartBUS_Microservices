package club.smartbus.service;

import club.smartbus.boundaries.routes.RouteResponse;
import club.smartbus.boundaries.stops.StopsRequest;
import reactor.core.publisher.Flux;

public interface RoutesService {
    Flux<RouteResponse> getRoutesWithIntermediateStations(StopsRequest stopsRequest);
}
