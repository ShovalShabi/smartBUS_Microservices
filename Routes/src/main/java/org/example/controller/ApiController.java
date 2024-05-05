package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.boundary.RouteRequest;
import org.example.logic.RoutesApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api")
public class ApiController {
    private final RoutesApiService routesApiService;

    @Autowired
    public ApiController(RoutesApiService routesApiService) {
        this.routesApiService = routesApiService;
    }

    @PostMapping(
            path = "/route")
    public Mono<Void> createRouteRequest(@RequestBody RouteRequest routeRequest) {
        routesApiService.getRouteFromApi(routeRequest);
        return Mono.empty();
    }
}
