package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents detailed information about a transit trip, including times, agency, line number, stop counts, and stop details.
 * This class encapsulates the core details of a transit trip, providing a summary of the transit line and its stops.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransitDetails {

    /**
     * The time of arrival at the final destination or a specific stop.
     * This field holds the arrival time as a string (in a suitable format, such as ISO 8601 or a simple time string).
     */
    private String arrivalTime;

    /**
     * The time of departure from the origin or a specific stop.
     * This field holds the departure time as a string (in a suitable format, such as ISO 8601 or a simple time string).
     */
    private String departureTime;

    /**
     * The name of the transportation agency responsible for the transit line.
     * This field represents the operator or agency that runs the transit service.
     */
    private String agency;

    /**
     * The transit line number or identifier.
     * This field holds the number or code that uniquely identifies the bus or transit line.
     */
    private String lineNumber;

    /**
     * The total number of stops along the transit line.
     * This field indicates the number of stops the transit vehicle makes on its route.
     */
    private Integer stopCounts;

    /**
     * Detailed information about the stops on the transit route, including arrival and departure stops.
     * This field contains a {@link StopDetails} object that provides in-depth data on the stops involved in the transit.
     */
    private StopDetails stopDetails;
}
