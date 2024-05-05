package org.example.controller;

import org.example.boundary.RouteRequest;
import org.example.dto.Routes;
import org.example.logic.GoogleApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api")
public class ApiController {
    private final GoogleApiService googleApiService;

    @Autowired
    public ApiController(GoogleApiService googleApiService) {
        this.googleApiService = googleApiService;
    }

    @PostMapping(
            path = "/route")
    public Mono<Routes> createRouteRequest(@RequestBody RouteRequest routeRequest) {
        return googleApiService.getRouteFromApi(routeRequest).log();
    }
}
