package club.smartbus.dto.websocket;

import club.smartbus.dto.transit.LatLng;
import club.smartbus.utils.WebSocketOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a WebSocket message sent by a passenger, containing details such as the start and end locations,
 * WebSocket options, and the message payload.
 * This class is used to handle WebSocket communication between the passenger client and the server.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerWSMessage implements OrderBusWSMessage {

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
     * Converts this {@link PassengerWSMessage} object to its JSON string representation.
     *
     * @return A JSON string representation of this {@link PassengerWSMessage}.
     * @throws JsonProcessingException if an error occurs while serializing the object to JSON.
     */
    @Override
    public String toJSONString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
