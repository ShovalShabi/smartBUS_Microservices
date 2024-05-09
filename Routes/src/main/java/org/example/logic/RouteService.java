package org.example.logic;

import org.example.boundary.RouteRequest;
import org.example.boundary.RouteResponse;
import reactor.core.publisher.Flux;

public interface RouteService {
    public Flux<RouteResponse> getRoute(RouteRequest routeRequest, Boolean isSorted);
}
