package club.smartbus.dto.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Interface representing a WebSocket message used in the order bus system.
 * Classes implementing this interface should provide methods for serializing
 * the message to JSON format and retrieving the message payload.
 */
public interface OrderBusWSMessage {

    /**
     * Converts the WebSocket message to a JSON string representation.
     *
     * @return The JSON string representation of the message.
     * @throws JsonProcessingException if the message cannot be serialized to JSON.
     */
    String toJSONString() throws JsonProcessingException;

    /**
     * Retrieves the payload of the WebSocket message.
     *
     * @return The payload of the message as a string.
     */
    String getPayload();
}
