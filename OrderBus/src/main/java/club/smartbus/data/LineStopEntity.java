package club.smartbus.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

/**
 * Represents a bus stop entity associated with a specific bus line.
 * This entity is mapped to the "line_stops" table in the database and contains
 * information about the bus line number, stop details, and its geographical location.
 */
@Table("line_stops")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineStopEntity {

    /**
     * The bus line number associated with this stop.
     * This field is part of the primary key and represents the unique identifier of the bus line.
     */
    @Column("line_number")
    private String lineNumber;     // PK line number of the bus

    /**
     * The name of the bus stop.
     * This field is part of the primary key and represents the unique name of the bus stop.
     */
    @Column("stop_name")
    private String stopName;       // PK stop name of the bus

    /**
     * The order of the stop along the bus line.
     * This field indicates the sequence of the stop in the bus route.
     */
    @Column("stop_order")
    private Integer stopOrder;     // Order of the stop in the sequence

    /**
     * The latitude coordinate of the bus stop.
     * This represents the geographical latitude of the stop's location.
     */
    @Column("lat")
    private Double lat;            // Latitude of the stop

    /**
     * The longitude coordinate of the bus stop.
     * This represents the geographical longitude of the stop's location.
     */
    @Column("lng")
    private Double lng;            // Longitude of the stop

    /**
     * The name of the transportation agency responsible for the bus line and stop.
     * This field represents the agency operating the bus service.
     */
    @Column("agency_name")
    private String agencyName;     // Agency operating the stop

    /**
     * Compares this bus stop entity to another object to check if they are equal.
     * The comparison is based on the bus line number, stop name, and stop order.
     *
     * @param o The object to be compared with this bus stop entity.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineStopEntity that = (LineStopEntity) o;
        return Objects.equals(lineNumber, that.lineNumber) &&
                Objects.equals(stopName, that.stopName) &&
                Objects.equals(stopOrder, that.stopOrder);
    }

    /**
     * Generates a hash code for this bus stop entity based on the line number, stop name, and stop order.
     *
     * @return The hash code for this bus stop entity.
     */
    @Override
    public int hashCode() {
        return Objects.hash(lineNumber, stopName, stopOrder);
    }
}
