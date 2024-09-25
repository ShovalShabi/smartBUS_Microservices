package club.smartbus.logic;

import club.smartbus.boundary.RouteRequest;
import club.smartbus.dto.polyline.PolylineObject;
import club.smartbus.dto.transit.TransitObject;
import reactor.core.publisher.Mono;

public interface GoogleApiService {
    public Mono<TransitObject> getTransitFromApi(RouteRequest routeRequest);

    public Mono<PolylineObject> getPolylineFromApi(RouteRequest routeRequest);
}
