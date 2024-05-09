package org.example.logic;

import com.google.maps.internal.PolylineEncoding;
import lombok.extern.slf4j.Slf4j;
import org.example.boundary.RouteRequest;
import org.example.dto.polyline.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class PolylineServiceImpl implements PolylineService {
    private final GoogleApiService googleApiService;

    @Autowired
    public PolylineServiceImpl(GoogleApiService googleApiService) {
        this.googleApiService = googleApiService;
    }

    /**
     * Retrieves the polyline from the Google Maps API.
     * @param routeRequest RouteRequest object
     * @return Mono of List of List of LatLng
     */
    @Override
    public Mono<List<List<LatLng>>> getPolyline(RouteRequest routeRequest) {
        return groupPolylineByList(googleApiService.getPolylineFromApi(routeRequest))
                .doOnError(error -> log.error("Failed to retrieve polyline", error))
                .doOnSuccess(polyline -> log.info("Successfully retrieved polyline"));
    }

    /**
     * Groups the decoded polylines by route.
     * @param polyMono Mono of PolylineObject
     * @return Mono of List of List of LatLng
     */
    private Mono<List<List<LatLng>>> groupPolylineByList(Mono<PolylineObject> polyMono) {
        return polyMono.map(PolylineObject::getRoutes) // Extract routes
                .flatMapMany(Flux::fromIterable) // Flatten the list of routes
                .flatMap(wrapper -> { // Flatten the list of PolylineWrappers for each route
                        String encodedPolyline = wrapper.getPolyline().getEncodedPolyline();
                        return Flux.fromIterable(PolylineEncoding.decode(encodedPolyline)) // Decode polyline
                        .map(LatLng::new) // Create LatLng objects
                        .collectList(); // Collect decoded LatLngs for each route
                })
                .collectList(); // Collect List of decoded LatLngs
    }
}
