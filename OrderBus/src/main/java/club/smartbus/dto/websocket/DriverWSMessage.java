package club.smartbus.dto.websocket;

import club.smartbus.utils.WebSocketOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketMessage;
import club.smartbus.dto.transit.LatLng;

import java.util.List;

/**
 * Represents a WebSocket message for the driver, containing details such as the agency, line number,
 * target station, and listeners' station states. This class is used to send and manage WebSocket messages
 * related to the driver's interaction with the transit system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverWSMessage implements WebSocketMessage<String> {

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
    private LatLng targetStation; // Mostly Null, will be valid on for accepting ride or cancelling ride

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
    public List<StationState> getVisitedStations() {
        return listenersStations
                .stream()
                .filter(stationState -> !stationState.getVisited())
                .toList();
    }

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
     * Indicates whether this is the last message in a WebSocket sequence.
     * Since each message is sent independently, this method always returns true.
     *
     * @return {@code true} as each message is considered the last in the sequence.
     */
    @Override
    public boolean isLast() {
        return true; // We will always send one message at a time so every one message is the last
    }
}
