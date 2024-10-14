package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents a geographic location using latitude and longitude.
 * This class provides methods for managing and comparing latitude and longitude values with precision to 3 decimal places.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LatLng {

    /**
     * The latitude coordinate of the location.
     * Represents the geographic latitude in decimal degrees.
     */
    private Double latitude;

    /**
     * The longitude coordinate of the location.
     * Represents the geographic longitude in decimal degrees.
     */
    private Double longitude;

    /**
     * Helper method to round a double value to 3 decimal places.
     * This method is used for rounding latitude and longitude values when comparing them for equality.
     *
     * @param value the double value to round.
     * @return the rounded value as a {@link BigDecimal}, or {@code null} if the input value is {@code null}.
     */
    private BigDecimal roundToThreeDecimals(Double value) {
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf(value).setScale(3, RoundingMode.HALF_UP);
    }

    /**
     * Compares this {@link LatLng} object to another object for equality.
     * The comparison is done by rounding both the latitude and longitude values to 3 decimal places.
     *
     * @param o the object to be compared with this {@link LatLng}.
     * @return {@code true} if the objects are equal (i.e., their latitude and longitude values are the same when rounded to 3 decimal places); {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatLng latLng = (LatLng) o;

        // Compare latitude and longitude values rounded to 3 decimal places
        return Objects.equals(roundToThreeDecimals(latitude), roundToThreeDecimals(latLng.latitude)) &&
                Objects.equals(roundToThreeDecimals(longitude), roundToThreeDecimals(latLng.longitude));
    }

    /**
     * Generates a hash code for this {@link LatLng} object based on its latitude and longitude.
     * The hash code is generated without rounding, using the raw values.
     *
     * @return the hash code for this {@link LatLng}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
