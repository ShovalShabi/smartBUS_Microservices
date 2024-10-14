package club.smartbus.service;

import club.smartbus.dto.routes.RouteResponseDTO;
import club.smartbus.dto.stops.StopsRequestDTO;
import reactor.core.publisher.Flux;

/**
 * Service interface for managing route-related operations.
 * This service handles the retrieval of routes with their intermediate stations based on the provided request.
 */
public interface RoutesService {

    /**
     * Retrieves routes with their associated intermediate stations.
     *
     * <p>Based on the provided {@link StopsRequestDTO}, this method fetches the available routes and includes details about
     * the intermediate stations between the origin and destination stops. The returned data is reactive, allowing
     * for non-blocking streaming of routes and their details.</p>
     *
     * @param stopsRequestDTO The request containing the necessary details (such as origin, destination, and additional parameters)
     *                     to retrieve the appropriate routes.
     * @return A {@link Flux< RouteResponseDTO >} containing the route information, including intermediate stations.
     */
    Flux<RouteResponseDTO> getRoutesWithIntermediateStations(StopsRequestDTO stopsRequestDTO);
}
