package org.example.logic;

import org.example.boundary.RouteRequest;
import org.example.dto.polyline.PolylineObject;
import org.example.dto.transit.TransitObject;
import reactor.core.publisher.Mono;

public interface GoogleApiService {
    public Mono<TransitObject> getTransitFromApi(RouteRequest routeRequest);
    public Mono<PolylineObject> getPolylineFromApi(RouteRequest routeRequest);
}
