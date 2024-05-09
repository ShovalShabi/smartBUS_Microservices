package org.example.logic;

import org.example.boundary.RouteRequest;
import org.example.dto.transit.TransitDetails;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransitService {
    public Mono<List<List<TransitDetails>>> getTransitDetails(RouteRequest routeRequest);
}
