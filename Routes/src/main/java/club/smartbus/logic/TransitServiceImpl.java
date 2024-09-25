package club.smartbus.logic;

import club.smartbus.boundary.RouteRequest;
import club.smartbus.dto.transit.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TransitServiceImpl implements TransitService {
    private final GoogleApiService googleApiService;

    @Autowired
    public TransitServiceImpl(GoogleApiService googleApiService) {
        this.googleApiService = googleApiService;
    }

    @Override
    public Mono<List<List<TransitDetails>>> getTransitDetails(RouteRequest routeRequest) {
        return groupTransitDetailsByList(googleApiService.getTransitFromApi(routeRequest))
                .onErrorResume(error -> Mono.just(List.of()))
                .doOnSuccess(transitDetails -> log.info("Successfully retrieved transit details"));
    }

    /**
     * Get the steps from the legs and combine them into a single list
     *
     * @param routesMono The routes to get the steps from
     * @return A Mono of the combined steps
     */
    private Mono<List<List<TransitDetails>>> groupTransitDetailsByList(Mono<TransitObject> routesMono) {
        // Get the legs from the routes
        Mono<List<Leg>> legsMono = combineRouteList(routesMono);

        // Get the transit details from the steps and group them by leg
        return legsMono.flatMap(legs -> {
                    List<List<TransitDetails>> groupedTransitDetails = new ArrayList<>();
                    try {
                        for (Leg leg : legs) {
                            List<TransitDetails> legTransitDetails =
                                    leg.getSteps().stream()
                                            .map(Step::getTransitDetails) // Get transit details for each step
                                            .filter(Objects::nonNull) // Filter out null transit details lists
                                            .toList(); // Collect into a list
                            // Add the list of transit details to the grouped list
                            groupedTransitDetails.add(legTransitDetails);
                        }
                        // Return the grouped transit details
                        return Mono.just(groupedTransitDetails);
                    } catch (Exception e) {
                        log.error("Error grouping transit details by list", e);
                        return Mono.error(e); // Propagate the exception
                    }
                })
                .doOnError(error -> log.error("Failed to group transit details by list", error))
                .onErrorResume(error -> Mono.just(List.of())); // Provide a fallback value in case of error
    }

    /**
     * Get the legs from the routes and combine them into a single list
     *
     * @param routesMono The routes to get the legs from
     * @return A Mono of the combined legs
     */
    private Mono<List<Leg>> combineRouteList(Mono<TransitObject> routesMono) {
        return routesMono.flatMap(routes -> {
                    try {
                        // Get the legs from the routes as a Flux
                        Flux<Leg> legsFlux = Flux.fromIterable(routes.getRoutes())
                                .flatMapIterable(Route::getLegs); // Flatten the list of legs

                        // Collect all legs into a single list
                        return legsFlux.collectList();
                    } catch (Exception e) {
                        log.error("Error combining route list", e);
                        return Mono.error(e); // Propagate the exception
                    }
                })
                .doOnError(error -> log.error("Failed to combine route list", error))
                .onErrorResume(error -> Mono.just(List.of())); // Provide a fallback value in case of error
    }
}
