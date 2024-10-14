package club.smartbus.service;

import club.smartbus.dto.routes.RouteResponse;
import club.smartbus.dto.stops.StopsRequest;
import reactor.core.publisher.Flux;

/**
 * Service interface for managing route-related operations.
 * This service handles the retrieval of routes with their intermediate stations based on the provided request.
 */
public interface RoutesService {

    /**
     * Retrieves routes with their associated intermediate stations.
     *
     * <p>Based on the provided {@link StopsRequest}, this method fetches the available routes and includes details about
     * the intermediate stations between the origin and destination stops. The returned data is reactive, allowing
     * for non-blocking streaming of routes and their details.</p>
     *
     * @param stopsRequest The request containing the necessary details (such as origin, destination, and additional parameters)
     *                     to retrieve the appropriate routes.
     * @return A {@link Flux<RouteResponse>} containing the route information, including intermediate stations.
     */
    Flux<RouteResponse> getRoutesWithIntermediateStations(StopsRequest stopsRequest);
}
