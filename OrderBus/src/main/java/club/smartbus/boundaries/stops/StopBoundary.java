package club.smartbus.boundaries.stops;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a stop along a bus line with its details such as name, order, and coordinates.
 * This class acts as a Data Transfer Object (DTO) used to encapsulate the information of a single stop
 * along a specified bus line.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopBoundary {

    /**
     * The bus line number that this stop belongs to.
     * This field identifies the line on which this stop is located.
     */
    private String lineNumber;

    /**
     * The name of the stop.
     * This typically contains the official or commonly used name of the bus stop.
     */
    private String stopName;

    /**
     * The order of the stop on the line.
     * This field represents the position of the stop in the sequence of stops along the bus line.
     * It starts from 1 for the first stop and increments for subsequent stops.
     */
    private Integer stopOrder;

    /**
     * The latitude coordinate of the stop.
     * This represents the geographic latitude of the stop's location.
     */
    private Double lat;

    /**
     * The longitude coordinate of the stop.
     * This represents the geographic longitude of the stop's location.
     */
    private Double lng;
}
