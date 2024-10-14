package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Represents detailed information about a transit stop, including arrival and departure stops, times, and intermediate stations.
 * This class holds data about a particular stop, including both the arrival and departure stop details, times, and an optional list of stations.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopDetails {

    /**
     * The stop where the transit vehicle arrives.
     * This field contains the details of the arrival stop, including its name and location.
     */
    private Stop arrivalStop;

    /**
     * The stop where the transit vehicle departs.
     * This field contains the details of the departure stop, including its name and location.
     */
    private Stop departureStop;

    /**
     * The time of arrival at the arrival stop.
     * This field holds the arrival time as a string (in a suitable time format, such as ISO 8601 or a simple time string).
     */
    private String arrivalTime;

    /**
     * The time of departure from the departure stop.
     * This field holds the departure time as a string (in a suitable time format, such as ISO 8601 or a simple time string).
     */
    private String departureTime;

    /**
     * An optional list of intermediate stations between the arrival and departure stops.
     * This field may contain a list of {@link Station} objects representing stations between the two stops, or it may be empty if no intermediate stations exist.
     */
    private Optional<List<Station>> stations;
}
