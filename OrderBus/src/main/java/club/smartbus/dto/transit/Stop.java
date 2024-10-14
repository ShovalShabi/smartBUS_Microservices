package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a stop along a bus or transit route.
 * This class contains information about the stop's name and its geographical location.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Stop {

    /**
     * The name of the stop.
     * This field holds the name or identifier of the stop.
     */
    private String name;

    /**
     * The {@link Location} object representing the geographical coordinates of the stop.
     * This field contains the latitude and longitude of the stop's location.
     */
    private Location location;
}
