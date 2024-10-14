package club.smartbus.dto.stops;

import club.smartbus.dto.routes.RouteResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a response containing bus stop information along with route details.
 * This class acts as a Data Transfer Object (DTO) for sending stop-related data back to the client,
 * including a {@link RouteResponseDTO} that encapsulates the relevant route information.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopsResponseDTO {

    /**
     * The response containing route details such as origin, destination, and time information.
     * This field encapsulates the route information associated with the requested stops.
     */
    private RouteResponseDTO routeResponseDTO;
}
