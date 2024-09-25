package club.smartbus.logic;

import club.smartbus.boundary.RouteRequest;
import club.smartbus.dto.polyline.LatLng;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PolylineService {
    public Mono<List<List<LatLng>>> getPolyline(RouteRequest routeRequest);
}
