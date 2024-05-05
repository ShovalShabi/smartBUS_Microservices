package org.example.logic;

import lombok.extern.slf4j.Slf4j;
import org.example.api.RoutesAPIConsumer;
import org.example.boundary.RouteRequest;
import org.example.dto.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class GoogleApiServiceImpl implements GoogleApiService{
    private final RoutesAPIConsumer apiConsumer;

    @Autowired
    public GoogleApiServiceImpl(@Qualifier("ApiConsumer") RoutesAPIConsumer apiConsumer) {
        this.apiConsumer = apiConsumer;
    }

    /***
     * Get the route from the API consumer
     * @param routeRequest The RouteRequest object containing the origin and destination addresses
     * @return The Route object containing the routes
     */
    @Override
    public Mono<Routes> getRouteFromApi(RouteRequest routeRequest) {
        try {
            // Get the route from the API consumer
            Routes newRoute = apiConsumer
                    .getRoutesFromAPI(routeRequest);

            // Clean the new route object request
            Mono<Routes> cleanedRoute = cleanNewObjectRequest(newRoute);
            log.info("Route received from API: {} At: {}",
                    routeRequest, LocalDateTime.now().toString());
            return cleanedRoute;
        } catch (Exception e) {
            log.error("Error getting route from API", e);
            return Mono.error(e);
        }
    }

    /***
     * This method creates a JSON request body for the Directions API
     * @param newRoute The new route object
     * @return The JSON request body
     */
    private Mono<Routes> cleanNewObjectRequest(Routes newRoute) {
        // Remove null steps from the response
        newRoute.getRoutes().forEach(route -> {
            route.getLegs().forEach(leg -> {
                leg.getSteps().removeIf(Objects::isNull);
            });
        });

        return Mono.just(newRoute);
    }
}
