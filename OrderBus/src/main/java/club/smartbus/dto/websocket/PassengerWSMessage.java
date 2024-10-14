package club.smartbus.dto.websocket;

import club.smartbus.dto.transit.LatLng;
import club.smartbus.utils.WebSocketOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketMessage;

/**
 * Represents a WebSocket message sent by a passenger, containing details such as the start and end locations,
 * WebSocket options, and the message payload.
 * This class is used to handle WebSocket communication between the passenger client and the server.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerWSMessage implements WebSocketMessage<String> {

    /**
     * The start location of the passenger, represented by latitude and longitude.
     */
    private LatLng startLocation;

    /**
     * The end location of the passenger, represented by latitude and longitude.
     */
    private LatLng endLocation;

    /**
     * Options for handling the WebSocket message, such as message type or priority.
     */
    private WebSocketOptions option;

    /**
     * The message payload containing the data being transmitted in the WebSocket message.
     */
    private String payload;

    /**
     * Returns the length of the payload in the WebSocket message.
     *
     * @return The length of the payload as an integer.
     */
    @Override
    public int getPayloadLength() {
        return payload.length();
    }

    /**
     * Indicates whether this is the last message in the WebSocket sequence.
     * In the case of passenger messages, multiple messages may be sent in sequence, so this method returns false.
     *
     * @return {@code false} since multiple messages may be sent in sequence.
     */
    @Override
    public boolean isLast() {
        return false;
    }
}
