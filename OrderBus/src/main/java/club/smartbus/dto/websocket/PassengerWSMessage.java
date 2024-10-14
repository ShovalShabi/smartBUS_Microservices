package club.smartbus.dto.websocket;

import club.smartbus.dto.transit.LatLng;
import club.smartbus.utils.WebSocketOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketMessage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerWSMessage implements WebSocketMessage {
    private LatLng startLocation;
    private LatLng endLocation;
    private WebSocketOptions option;
    private String payload;

    @Override
    public int getPayloadLength() {
        return payload.length();
    }

    @Override
    public boolean isLast() {
        return false;
    }
}
