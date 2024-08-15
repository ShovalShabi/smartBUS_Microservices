package org.example.boundaries.websocket.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.utils.Location;
import org.example.utils.WebSocketOptions;
import org.springframework.web.socket.WebSocketMessage;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebSocketMessageBoundary implements WebSocketMessage {
    private Location currentLocation;
    private Location targetLocation;
    private WebSocketOptions option;
    private String payload;
    @Override
    public Object getPayload() {
        return payload;
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
