package org.example.controller;

import org.example.boundary.RouteRequest;
import org.example.boundary.RouteResponse;
import org.example.logic.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/api")
public class RouteController {
    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping(
            path = "/route")
    public Flux<RouteResponse> createRouteRequest(@RequestBody RouteRequest routeRequest) {
        return routeService.getRoute(routeRequest).log();
    }
}
