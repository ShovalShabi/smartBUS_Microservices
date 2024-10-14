package club.smartbus.dto.websocket;

import club.smartbus.dto.transit.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Represents the state of a station in the context of WebSocket communication, including whether the station has been visited.
 * This class holds information about the station and its visited status for tracking purposes in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationState {

    /**
     * The {@link Station} object representing the transit station.
     * This field contains the station's details, including name, location, and order.
     */
    private Station data;

    /**
     * A flag indicating whether the station has been visited.
     * If {@code true}, the station has already been visited; otherwise, it has not been visited.
     */
    private Boolean visited;

    /**
     * Compares this {@link StationState} object to another object for equality.
     * The comparison is based on the {@link Station} data.
     *
     * @param o the object to compare with this {@link StationState}.
     * @return {@code true} if the station data is the same, otherwise {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationState that = (StationState) o;
        return data.equals(that.data);
    }

    /**
     * Generates a hash code for this {@link StationState} based on the station data and visited status.
     *
     * @return The hash code for this {@link StationState}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(data, visited);
    }
}
