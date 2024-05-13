package org.example.controller;

import com.netflix.discovery.EurekaClient;
import org.example.boundary.RouteRequest;
import org.example.boundary.RouteResponse;
import org.example.logic.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/route")
public class RouteController {
    private final RouteService routeService;

    @Autowired
    private EurekaClient eurekaClient;

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
