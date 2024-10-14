package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a location defined by latitude and longitude coordinates.
 * This class encapsulates the geographical location using a {@link LatLng} object.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {

    /**
     * The {@link LatLng} object representing the geographical coordinates (latitude and longitude) of the location.
     */
    private LatLng latLng;
}
