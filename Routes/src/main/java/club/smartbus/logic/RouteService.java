package club.smartbus.logic;

import club.smartbus.boundary.RouteRequest;
import club.smartbus.boundary.RouteResponse;
import reactor.core.publisher.Flux;

public interface RouteService {
    public Flux<RouteResponse> getRoute(RouteRequest routeRequest, Boolean isSorted);
}
