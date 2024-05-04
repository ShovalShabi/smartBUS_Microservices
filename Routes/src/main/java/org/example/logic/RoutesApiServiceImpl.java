package org.example.logic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.RoutesAPIConsumer;
import org.example.boundary.RouteRequest;
import org.example.dto.Route;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoutesApiServiceImpl implements RoutesApiService{
    private final RoutesAPIConsumer apiConsumer;

    @Override
    public Mono<Route> getRouteFromApi(RouteRequest routeRequest) {
        String origin = routeRequest.getOriginAddress();
        String destination = routeRequest.getDestinationAddress();

        Route newRoute = apiConsumer
                .getRoutesFromAPI(origin, destination);
        log.info("API Route Request from {} to {} is {}, TIME: {}",
                origin, destination, newRoute, LocalDateTime.now().toString());

        System.err.println(newRoute.toString());

        return Mono.just(newRoute);
    }
}
