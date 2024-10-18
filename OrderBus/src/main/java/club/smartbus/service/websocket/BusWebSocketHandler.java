package club.smartbus.service.websocket;

import club.smartbus.dto.transit.LatLng;
import club.smartbus.dto.websocket.DriverWSMessage;
import club.smartbus.dto.websocket.OrderBusWSMessage;
import club.smartbus.dto.websocket.PassengerWSMessage;
import club.smartbus.service.bus.BusService;
import club.smartbus.utils.WebSocketOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * WebSocket handler responsible for managing WebSocket connections and messages
 * between the OrderBusClient (passenger) and DriverConsole (driver) clients.
 * Handles the lifecycle of WebSocket sessions, message broadcasting, and message processing.
 */
@Slf4j
@AllArgsConstructor
@Data
@Component
public class BusWebSocketHandler implements WebSocketHandler {

    /**
     * Object mapper used to convert incoming WebSocket messages to Java objects.
     */
    private final ObjectMapper objectMapper;

    /**
     * Manages WebSocket sessions, including storing and broadcasting messages.
     */
    private final WebSocketSessionManager sessionManager;

    /**
     * Service responsible for bus-related logic, such as retrieving bus lines and stations.
     */
    @Autowired
    private final BusService busService;

    /**
     * Called when a WebSocket connection is established. Adds the session to Redis.
     *
     * @param session the WebSocket session being established.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            String clientType = getClientTypeFromSession(session);
            sessionManager.addSession(session); // Add session to Redis
            log.info("WebSocket connection {} with client {} established", session.getId(), clientType);

        } catch (Exception e) {
            log.error("Failed to establish WebSocket connection: {}", e.getMessage());
        }
    }

    /**
     * Handles incoming WebSocket messages for both OrderBusClient (passenger) and DriverConsole (driver) clients.
     *
     * @param session the WebSocket session.
     * @param message the incoming WebSocket message.
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        try {
            String clientType = getClientTypeFromSession(session);

            log.info("Received message from session {} as {} client", session.getId(), clientType);

            switch (clientType) {
                case "OrderBusClient" -> handlePassengerMessage(session, message);
                case "DriverConsole" -> handleDriverMessage(session, message);
                default -> log.error("Unknown client type: {} for session {}", clientType, session.getId());
            }

        } catch (Exception e) {
            log.error("Failed to handle WebSocket message: {} for session: {}", e.getMessage(), session.getId());
        }
    }

    /**
     * Handles messages from the OrderBusClient (passenger).
     *
     * @param session the WebSocket session.
     * @param message the incoming WebSocket message.
     */
    private void handlePassengerMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        PassengerWSMessage passengerWSMessage = objectMapper.readValue(message.getPayload().toString(), PassengerWSMessage.class);

        switch (passengerWSMessage.getOption()) {
            case REQUEST_BUS -> {
                log.info("Passenger {} attempt to request a bus from {} to {} ...",
                        session.getId(), passengerWSMessage.getStartLocation(), passengerWSMessage.getEndLocation());
                // Store the passenger's message in Redis
                sessionManager.setDataPayload(passengerWSMessage, session);

                // Cross-check the bus lines that go through the start and end locations
                Set<String> relevantBusLines = busService.getRelevantBusLineByStartAndDestinationLocation(
                                passengerWSMessage.getStartLocation(), passengerWSMessage.getEndLocation())
                        .collect(Collectors.toSet())
                        .block();  // Block to wait for the result

                // Retrieve the station name for broadcasting
                String stationName = busService
                        .findStationNameByLatitudeAndLongitude(passengerWSMessage
                                .getStartLocation())
                        .block();

                // Find relevant driver sessions for the bus lines and stations
                List<WebSocketSession> driverSessions = sessionManager
                        .getListOfDriverSessionsByBusLinesAndStation(relevantBusLines, passengerWSMessage.getStartLocation());

                // Create the message to broadcast to drivers
                String messageText = String.format("A passenger in %s is waiting for a pick-up, approve?", stationName);
                PassengerWSMessage broadcastMessage = new PassengerWSMessage(null, null, WebSocketOptions.REQUEST_BUS, messageText);

                // Broadcast the message to relevant drivers
                broadcastMessage(driverSessions, broadcastMessage, "OrderBusClient");

                log.info("Passenger {} requested a bus from {} to {}. Message broadcast to relevant drivers.",
                        session.getId(), passengerWSMessage.getStartLocation(), passengerWSMessage.getEndLocation());
            }
            case CANCELING_RIDE -> {
                // Store the passenger's cancellation in Redis
                sessionManager.setDataPayload(passengerWSMessage, session);

                // Cross-check the bus lines that go through the start and end locations
                Set<String> relevantBusLines = busService.getRelevantBusLineByStartAndDestinationLocation(
                                passengerWSMessage.getStartLocation(), passengerWSMessage.getEndLocation())
                        .collect(Collectors.toSet())
                        .block();  // Block to wait for the result

                // Retrieve the station name for broadcasting
                String stationName = busService.findStationNameByLatitudeAndLongitude(passengerWSMessage.getStartLocation()).block();

                // Find relevant driver sessions for the bus lines and stations
                List<WebSocketSession> driverSessions = sessionManager.getListOfDriverSessionsByBusLinesAndStation(relevantBusLines, passengerWSMessage.getStartLocation());

                // Create the cancellation message to broadcast to drivers
                String messageText = String.format("A passenger in %s has canceled their ride", stationName);
                PassengerWSMessage cancellationMessage = new PassengerWSMessage(null, null, WebSocketOptions.CANCELING_RIDE, messageText);

                // Broadcast the cancellation message to relevant drivers
                broadcastMessage(driverSessions, cancellationMessage, "OrderBusClient");

                log.info("Passenger {} canceled their ride from {}. Cancellation message broadcast to relevant drivers.",
                        session.getId(), passengerWSMessage.getStartLocation());
            }
            case KEEP_ALIVE -> {
                log.info("Passenger's session pinged the server {}", session.getId());
            }
            default -> log.error("Unknown message option from OrderBusClient with session: {}", session.getId());
        }
    }

    /**
     * Handles messages from the DriverConsole (driver).
     *
     * @param session the WebSocket session.
     * @param message the incoming WebSocket message.
     */
    private void handleDriverMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        DriverWSMessage driverWSMessage = objectMapper.readValue(message.getPayload().toString(), DriverWSMessage.class);

        switch (driverWSMessage.getOption()) {
            case ACCEPTING_RIDE -> {
                // Store the driver's updated message in Redis
                sessionManager.setDataPayload(driverWSMessage, session);

                // Look up passengers waiting at the driver's current location
                LatLng stationLocation = driverWSMessage.getTargetStation();
                List<WebSocketSession> passengerSessions = sessionManager.getListOfPassengerSessionsByLocation(stationLocation);

                // Create the message to broadcast
                String messageText = String.format("Bus from %s with line number %s is heading your way",
                        driverWSMessage.getAgency(), driverWSMessage.getLineNumber());
                PassengerWSMessage broadcastMessage = new PassengerWSMessage(null, null,
                        WebSocketOptions.ACCEPTING_RIDE, messageText);

                // Broadcast message to relevant passengers
                broadcastMessage(passengerSessions, broadcastMessage, "OrderBusClient");
                log.info("Driver {} accepted a ride to ({},{}).", session.getId(),
                        driverWSMessage
                                .getTargetStation()
                                .getLatitude(),
                        driverWSMessage
                                .getTargetStation()
                                .getLongitude());
            }
            case CANCELING_RIDE -> {
                // Store the driver's updated message in Redis
                sessionManager.setDataPayload(driverWSMessage, session);

                // Look up passengers waiting at the driver's current location
                LatLng stationLocation = driverWSMessage.getTargetStation();
                List<WebSocketSession> passengerSessions = sessionManager.getListOfPassengerSessionsByLocation(stationLocation);

                // Create the cancellation message to broadcast
                String messageText = String.format("Bus from %s with line number %s could not pick you up",
                        driverWSMessage.getAgency(), driverWSMessage.getLineNumber());
                PassengerWSMessage cancellationMessage = new PassengerWSMessage(null, null,
                        WebSocketOptions.CANCELING_RIDE, messageText);

                // Broadcast cancellation message to relevant passengers
                broadcastMessage(passengerSessions, cancellationMessage, "OrderBusClient");

                log.info("Driver {} canceled a ride to ({},{}).", session.getId(),
                        driverWSMessage
                                .getTargetStation()
                                .getLatitude(),
                        driverWSMessage
                                .getTargetStation()
                                .getLongitude());
            }
            case UPDATE_ROUTE_STEP -> {
                // Regularly update the driver's current location in Redis
                sessionManager.setDataPayload(driverWSMessage, session);
                log.info("Driver {} updated their route step.", session.getId());
            }
            default -> log.error("Unknown message option from DriverConsole with session: {}", session.getId());
        }
    }

    /**
     * Broadcasts a WebSocket message to a list of WebSocket sessions.
     *
     * @param sessions   the list of {@link WebSocketSession} to send the message to.
     * @param message    the {@link OrderBusWSMessage} to be broadcast (either {@link PassengerWSMessage} or {@link DriverWSMessage}).
     * @param clientType the type of client sending the message: "OrderBusClient" for passengers or "DriverConsole" for drivers.
     */
    public void broadcastMessage(List<WebSocketSession> sessions, OrderBusWSMessage message, String clientType) {
        for (WebSocketSession session : sessions) {
            sessionManager.sendMessage(session, message, clientType);
        }
    }

    /**
     * Handles transport errors in the WebSocket connection.
     *
     * @param session   the WebSocket session.
     * @param exception the error that occurred during communication.
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        sessionManager.closeSessionAndRemovePayload(session);
        log.error("Transport error for session {}: {}", session.getId(), exception.getMessage());
    }

    /**
     * Called when a WebSocket connection is closed.
     *
     * @param session     the WebSocket session that is closed.
     * @param closeStatus the status of the WebSocket closure.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        sessionManager.closeSessionAndRemovePayload(session);
        log.info("WebSocket connection {} is closed with status: {}", session.getId(), closeStatus);
    }

    /**
     * WebSocket server does not support partial messages.
     *
     * @return false since partial messages are not supported.
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * Extracts the client type (OrderBusClient or DriverConsole) from the session's query parameters.
     *
     * @param session the WebSocket session.
     * @return the client type (either "OrderBusClient" or "DriverConsole").
     */
    private String getClientTypeFromSession(WebSocketSession session) {
        Map<String, String> queryParams = getQueryParams(session);
        return queryParams.get("clientType");  // Extract the clientType from query params
    }

    /**
     * Retrieves the query parameters from the session's URI.
     *
     * @param session the WebSocket session.
     * @return a map of the query parameters.
     */
    private Map<String, String> getQueryParams(WebSocketSession session) {
        String query = Objects.requireNonNull(session.getUri()).getQuery();  // Get the query string
        return org.springframework.web.util.UriComponentsBuilder.fromUriString("?" + query)
                .build().getQueryParams().toSingleValueMap();  // Parse query parameters
    }
}
