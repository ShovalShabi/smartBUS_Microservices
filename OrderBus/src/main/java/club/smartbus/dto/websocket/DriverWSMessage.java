package club.smartbus.dto.websocket;

import club.smartbus.dto.transit.LatLng;
import club.smartbus.utils.WebSocketOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a WebSocket message for the driver, containing details such as the agency, line number,
 * target station, and listeners' station states. This class is used to send and manage WebSocket messages
 * related to the driver's interaction with the transit system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverWSMessage implements OrderBusWSMessage {

    /**
     * The name of the transportation agency operating the line.
     */
    private String agency;

    /**
     * The line number or identifier for the bus or transit line.
     */
    private String lineNumber;

    /**
     * The target station for the ride, represented by its latitude and longitude.
     * This field is mostly null and is only populated when accepting or canceling a ride.
     */
    private LatLng targetStation; // Mostly Null, will be valid only for accepting or canceling a ride

    /**
     * A list of stations that are listening to WebSocket updates, represented by their state.
     * Each station's state contains information about whether the station has been visited or not.
     */
    private List<StationState> listenersStations;

    /**
     * Options for handling the WebSocket message, such as message type or priority.
     */
    private WebSocketOptions option;

    /**
     * The message payload containing data being transmitted in the WebSocket message.
     */
    private String payload;

    /**
     * Retrieves a list of stations that have not yet been visited by filtering the {@link StationState} list.
     *
     * @return A list of stations that have not been visited.
     */
    public List<StationState> computeUnvisitedStations() {
        return listenersStations
                .stream()
                .filter(stationState -> !stationState.getVisited())
                .toList();
    }

    /**
     * Converts this object to a JSON string using Jackson's ObjectMapper.
     *
     * @return A JSON string representing the current state of this object.
     * @throws JsonProcessingException If an error occurs during serialization.
     */
    @Override
    public String toJSONString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
