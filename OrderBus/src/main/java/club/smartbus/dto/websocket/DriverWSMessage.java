package club.smartbus.dto.websocket;

import club.smartbus.utils.WebSocketOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketMessage;
import club.smartbus.dto.transit.LatLng;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverWSMessage implements WebSocketMessage {
    private String agency;
    private String lineNumber;
    private LatLng targetStation;// Mostly Null, will be valid on for accepting ride or cancelling ride
    private List<StationState> listenersStations;
    private WebSocketOptions option;
    private String payload;


    public List<StationState> getVisitedStations(){
        return listenersStations.
                stream().
                filter(stationState -> !stationState.getVisited())
                .toList();
    }

    @Override
    public int getPayloadLength() {
        return payload.length();
    }

    @Override
    public boolean isLast() {
        return true; // We will always send one message at a time so every one message is the last
    }
}
