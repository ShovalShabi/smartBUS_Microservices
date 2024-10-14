package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LatLng {
    private Double latitude;
    private Double longitude;


    /**
     * Helper method to round a double value to 3 decimal places.
     *
     * @param value the double value to round.
     * @return the rounded value as a BigDecimal.
     */
    private BigDecimal roundToThreeDecimals(Double value) {
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf(value).setScale(3, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatLng latLng = (LatLng) o;

        // Compare latitude and longitude values rounded to 3 decimal places
        return Objects.equals(roundToThreeDecimals(latitude), roundToThreeDecimals(latLng.latitude)) &&
                Objects.equals(roundToThreeDecimals(longitude), roundToThreeDecimals(latLng.longitude));
    }
    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
