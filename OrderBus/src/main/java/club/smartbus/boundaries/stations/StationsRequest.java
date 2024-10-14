package club.smartbus.boundaries.stations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request for station information based on a bus line and agency.
 * This class acts as a Data Transfer Object (DTO) for passing parameters from the client to the backend
 * to request information about bus stations along a particular line for a specific agency.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StationsRequest {

    /**
     * The bus line number for which the station information is requested.
     * This typically corresponds to the line identifier used by the agency.
     */
    private String lineNumber;

    /**
     * The name of the transportation agency responsible for the bus line.
     * This field specifies the agency that operates the requested line number.
     */
    private String agency;
}
