package org.example.logic;

import org.example.boundary.RouteRequest;
import org.example.dto.Routes;
import reactor.core.publisher.Mono;

public interface GoogleApiService {
    public Mono<Routes> getRouteFromApi(RouteRequest routeRequest);
}
