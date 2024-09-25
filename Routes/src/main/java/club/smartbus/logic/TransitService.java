package club.smartbus.logic;

import club.smartbus.boundary.RouteRequest;
import club.smartbus.dto.transit.TransitDetails;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransitService {
    public Mono<List<List<TransitDetails>>> getTransitDetails(RouteRequest routeRequest);
}
