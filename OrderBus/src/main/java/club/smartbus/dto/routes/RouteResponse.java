package club.smartbus.dto.routes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represents a response containing detailed route information.
 * This class is used as a Data Transfer Object (DTO) for sending route-related data from the backend to the client.
 * It contains essential route details such as origin, destination, departure and arrival times, and the route flow.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RouteResponse {

    /**
     * The origin or starting point of the route.
     * This field typically contains the name or identifier of the departure station or location.
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
     * The timestamp indicating when this route information was published or made available.
     * This field is used to track when the route was generated or officially published.
     */
    private String publishedTimestamp;

    /**
     * A map containing additional data about the route flow.
     * The keys in this map represent various aspects or segments of the route, while the values provide specific details
     * such as stops, durations, or other metadata relevant to the route's flow.
     */
    private Map<String, Object> routeFlow;
}
