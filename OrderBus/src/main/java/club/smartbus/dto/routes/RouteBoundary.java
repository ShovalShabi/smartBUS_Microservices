package club.smartbus.dto.routes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represents a boundary object for routes, encapsulating route information such as
 * origin, destination, departure and arrival times, and the overall route flow.
 * This class acts as a Data Transfer Object (DTO) used to pass route information between
 * different layers of the application.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RouteBoundary {

    /**
     * The origin or starting point of the route.
     * Typically, this will be the name or identifier of the departure station or location.
     */
    private String origin;

    /**
     * The destination or endpoint of the route.
     * This field represents the final stop or location for the route.
     */
    private String destination;

    /**
     * The initial departure time for the route.
     * This field captures the earliest possible departure time in a time format (e.g., ISO 8601).
     */
    private String initialDepartureTime;

    /**
     * The final arrival time at the destination.
     * This represents the latest expected arrival time at the destination in a time format (e.g., ISO 8601).
     */
    private String finalArrivalTime;

    /**
     * The timestamp indicating when this route was published or made available.
     * This field is typically used for tracking when the route information became official or was generated.
     */
    private String publishedTimestamp;

    /**
     * A map containing additional data about the route flow.
     * The keys represent various aspects or segments of the route, while the values provide specific details
     * (e.g., stops, durations, or other metadata relevant to the flow of the route).
     */
    private Map<String, Object> routeFlow;
}
