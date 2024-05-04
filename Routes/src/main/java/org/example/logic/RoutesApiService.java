package org.example.logic;

import org.example.boundary.RouteRequest;
import org.example.dto.Route;
import reactor.core.publisher.Mono;

public interface RoutesApiService {
    public Mono<Route> getRouteFromApi(RouteRequest routeRequest);
}
