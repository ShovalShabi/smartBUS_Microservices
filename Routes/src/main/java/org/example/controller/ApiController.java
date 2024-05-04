package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.boundary.RouteRequest;
import org.example.logic.RoutesApiService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {
    private final RoutesApiService routesApiService;

    @PutMapping(
            path = "/route")
    public Mono<Void> createRouteRequest(@RequestBody RouteRequest routeRequest) {
        routesApiService.getRouteFromApi(routeRequest).log();
        return Mono.empty();
    }
}
