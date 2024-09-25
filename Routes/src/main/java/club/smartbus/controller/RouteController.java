package club.smartbus.controller;

import club.smartbus.boundary.RouteRequest;
import club.smartbus.boundary.RouteResponse;
import club.smartbus.logic.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/route")
public class RouteController {
    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public Flux<RouteResponse> createRouteRequest(
            @RequestBody RouteRequest routeRequest,
            @RequestParam(name = "sorted", required = false, defaultValue = "true") Boolean isSorted) {
        return routeService.getRoute(routeRequest, isSorted);
    }
}
