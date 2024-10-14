package club.smartbus.dto.routes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request for route information.
 * This class is used to encapsulate the data required to find routes between an origin and a destination.
 * It acts as a Data Transfer Object (DTO) for the request being sent from the client to the backend.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RouteRequest {

    /**
     * The address or location from where the journey will start.
     * Typically, this field contains the full address or a recognized location name.
     */
    private String originAddress;

    /**
     * The address or location where the journey will end.
     * Similar to the origin, this field typically contains the full destination address or a recognized location name.
     */
    private String destinationAddress;
}
