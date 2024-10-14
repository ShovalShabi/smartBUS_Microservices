package club.smartbus.boundaries.stops;

import club.smartbus.boundaries.routes.RouteRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request for bus stops information, which can optionally include an order for a bus.
 * This class acts as a Data Transfer Object (DTO) for passing stop-related parameters,
 * including route information and a flag indicating whether to order a bus.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopsRequest {

    /**
     * A flag indicating whether a bus should be ordered.
     * If {@code true}, the request will include an order for a bus; otherwise, it will not.
     */
    private Boolean orderBus;

    /**
     * The route request containing details about the origin and destination.
     * This field encapsulates the route details that the request is associated with,
     * including origin and destination addresses.
     */
    private RouteRequest routeRequest;
}
