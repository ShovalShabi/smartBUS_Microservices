package org.example.service;

import org.example.boundaries.routes.RouteRequest;
import org.example.boundaries.routes.RouteResponse;
import org.example.boundaries.stops.StopsRequest;
import reactor.core.publisher.Flux;

public interface RoutesService {
    Flux<RouteResponse> fetchRoutes(StopsRequest stopsRequest);
    Flux<RouteResponse> getRoutesWithIntermediateStations(StopsRequest stopsRequest);
}
