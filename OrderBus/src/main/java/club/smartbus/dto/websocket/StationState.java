package club.smartbus.dto.websocket;

import club.smartbus.dto.transit.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationState {

    private Station data;

    private Boolean visited;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationState that = (StationState) o;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, visited);
    }
}
