package club.smartbus.boundaries.stops;

import club.smartbus.boundaries.routes.RouteResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a response containing bus stop information along with route details.
 * This class acts as a Data Transfer Object (DTO) for sending stop-related data back to the client,
 * including a {@link RouteResponse} that encapsulates the relevant route information.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopsResponse {

    /**
     * The response containing route details such as origin, destination, and time information.
     * This field encapsulates the route information associated with the requested stops.
     */
    private RouteResponse routeResponse;
}
