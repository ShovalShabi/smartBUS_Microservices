package org.example.logic;

import org.example.boundary.RouteRequest;
import org.example.dto.polyline.LatLng;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PolylineService {
    public Mono<List<List<LatLng>>> getPolyline(RouteRequest routeRequest);
}
