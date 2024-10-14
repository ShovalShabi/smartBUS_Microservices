package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a bus or transit station.
 * This class holds information about the station's name, location, and its order in the sequence of stops.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Station {

    /**
     * The name of the station.
     * This field represents the unique name or identifier of the station.
     */
    private String stationName;

    /**
     * The {@link Location} object representing the geographical coordinates of the station.
     * This field contains the latitude and longitude of the station.
     */
    private Location location;

    /**
     * The order of the stop along the bus or transit route.
     * This field indicates the station's position in the sequence of stops, starting from 1.
     */
    private Integer stopOrder;
}
