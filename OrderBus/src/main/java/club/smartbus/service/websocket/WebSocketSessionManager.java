package club.smartbus.service.websocket;

import club.smartbus.dto.transit.LatLng;
import club.smartbus.dto.websocket.DriverWSMessage;
import club.smartbus.dto.websocket.PassengerWSMessage;
import club.smartbus.utils.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Data
@Component
public class WebSocketSessionManager {

    private final RedisTemplate<String, Object> redisTemplateForData;
    private final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Autowired
    public WebSocketSessionManager(RedisTemplate<String, Object> redisTemplateForData) {
        this.redisTemplateForData = redisTemplateForData;
    }

    /**
     * Add a WebSocket session to the ConcurrentHashMap using session ID as key.
     *
     * @param session WebSocketSession to store.
     */
    public void addSession(WebSocketSession session) {
        if (session != null && session.isOpen()) {
            sessionMap.put(session.getId(), session);
            log.info("Session {} added to ConcurrentHashMap", session.getId());
        } else {
            log.warn("Attempted to add a session that is null or closed");
        }
    }

    /**
     * Add and open a WebSocket session message to Redis using session ID as key.
     * The payload can be either a PassengerWSMessage or DriverWSMessage.
     *
     * @param payload WebSocket payload to store.
     * @param session WebSocketSession used to retrieve the session ID.
     */
    public void setDataPayload(Object payload, WebSocketSession session) {
        if (session != null && session.isOpen()) {
            redisTemplateForData.opsForValue().set("data_" + session.getId(), payload);
            log.info("Session {} stored its payload {} to Redis", session.getId(), payload);
        } else {
            log.warn("Attempted to add a payload for a session that is null or closed");
        }
    }


    /**
     * Close and remove a WebSocket session and its payload from the ConcurrentHashMap and Redis.
     *
     * @param session WebSocketSession used to retrieve the session ID.
     */
    public void closeSessionAndRemovePayload(WebSocketSession session) {
        if (session != null) {
            try {
                session.close(); // Close the session
                redisTemplateForData.delete("data_" + session.getId()); // Removing the session payload from Redis
                sessionMap.remove(session.getId()); // Removing the session from the map
                log.info("Session {} closed and removed from ConcurrentHashMap and Redis", session.getId());
            } catch (IOException e) {
                log.error("Failed to close session {}: {}", session.getId(), e.getMessage());
            }
        } else {
            log.warn("Attempted to close and remove a null session");
        }
    }

    /**
     * Sends a WebSocket message to the specified session, using the client type to determine
     * whether to send a {@link PassengerWSMessage} or a {@link DriverWSMessage}.
     * The message is retried a fixed number of times in case of failure.
     *
     * @param session    The {@link WebSocketSession} to send the message to.
     * @param message    The {@link WebSocketMessage} to be sent (either {@link PassengerWSMessage} or {@link DriverWSMessage}).
     * @param clientType The type of client sending the message: "OrderBusClient" for passengers or "DriverConsole" for drivers.
     */
    public void sendMessage(WebSocketSession session, WebSocketMessage message, String clientType) {
        if (session != null && session.isOpen()) {
            TextMessage textMessage = null;
            switch (clientType) {
                case "OrderBusClient" -> {
                    PassengerWSMessage passengerWSMessage = (PassengerWSMessage) message;
                    textMessage = new TextMessage(passengerWSMessage.getPayload());
                }
                case "DriverConsole" -> {
                    DriverWSMessage driverWSMessage = (DriverWSMessage) message;
                    textMessage = new TextMessage(driverWSMessage.getPayload());

                }
            }
            if (textMessage == null)
                return;

            boolean success = sendMessageWithRetry(session, textMessage);
            if (!success) {
                log.error("Failed to send message to session {} after {} attempts", session.getId(), Constants.MAX_RETRIES);
                closeSessionAndRemovePayload(session); // Close session if message sending fails
            }
        } else {
            log.error("Attempted to send a message to a session that is not open or does not exist");
        }
    }

    /**
     * Helper method that sends a WebSocket message to the specified session with retry logic.
     * This method retries sending the message up to a maximum number of attempts.
     *
     * @param session The {@link WebSocketSession} to send the message to.
     * @param message The {@link TextMessage} to be sent.
     * @return true if the message was successfully sent, false if all retry attempts failed.
     */
    private boolean sendMessageWithRetry(WebSocketSession session, TextMessage message) {
        int attempt = 0;
        while (attempt < Constants.MAX_RETRIES - 1) {
            try {
                session.sendMessage(message);
                return true; // Message sent successfully
            } catch (IOException e) {
                log.warn("Attempt {}: Failed to send message to session {}. Retrying...", attempt + 1, session.getId(), e);
                attempt++;  // Retry if the max attempts have not been reached
            }
        }
        return false; // All retry attempts failed
    }

    /**
     * Retrieve all WebSocket sessions where the PassengerWSMessage has the given startLocation.
     *
     * @param coordinate the LatLng coordinate to match against the startLocation in PassengerWSMessage.
     * @return a list of WebSocket sessions of passengers with matching startLocation.
     */
    public List<WebSocketSession> getListOfPassengerSessionsByLocation(LatLng coordinate) {
        // Iterate over all keys with the pattern "data_*" to find PassengerWSMessage
        return Objects.requireNonNull(redisTemplateForData.keys("data_*")).stream()
                .map(key -> new AbstractMap.SimpleEntry<>(key.split("_")[1] // [0] contains the string  "data", [1] contains the session ID
                        ,redisTemplateForData.opsForValue().get(key))) // Create a Map.Entry with sessionId and its data
                .filter(entry -> entry.getValue() instanceof PassengerWSMessage) // Filter PassengerWSMessages
                .filter(entry -> coordinate.equals(((PassengerWSMessage) entry.getValue()).getStartLocation())) // Check if the startLocation matches
                .map(entry -> sessionMap.get(entry.getKey())) // Retrieve WebSocketSession from sessionMap using sessionId
                .filter(Objects::nonNull) // Ensure session is not null
                .collect(Collectors.toList()); // Collect the valid WebSocket sessions
    }



    /**
     * Retrieve all driver WebSocket sessions where the DriverWSMessage contains a lineNumber that matches one of the provided bus lines
     * and where the station represented by the LatLng object has not been visited yet.
     *
     * @param relevantBusLines a set of bus lines to match against the lineNumber in DriverWSMessage.
     * @param stationLocation  the LatLng object representing the station position.
     * @return a list of WebSocket sessions of drivers with matching line numbers and unvisited stations.
     */
    public List<WebSocketSession> getListOfDriverSessionsByBusLinesAndStation(Set<String> relevantBusLines, LatLng stationLocation) {

        // Iterate over all keys with the pattern "data_*" to find DriverWSMessage
        return Objects.requireNonNull(redisTemplateForData.keys("data_*")).stream()
                .map(key -> {
                    String sessionId = key.split("_")[1]; // [0] contains the string  "data", [1] contains the session ID
                    Object webSocketSession = redisTemplateForData.opsForValue().get("data_" + sessionId); // Get the Session Object
                    return new AbstractMap.SimpleEntry<>(sessionId, webSocketSession); // Return session ID and data as a pair
                })
                .filter(entry -> entry.getValue() instanceof DriverWSMessage) // Filter DriverWSMessages
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), (DriverWSMessage) entry.getValue())) // Cast value to DriverWSMessage
                .filter(entry -> relevantBusLines.contains(entry.getValue().getLineNumber())) // Check if line number is relevant
                .filter(entry -> entry.getValue().getListenersStations().stream()
                        .anyMatch(stationState -> stationState.getData().getLocation().getLatLng().equals(stationLocation))) // Check if stationLocation is in listeners
                .filter(entry -> entry.getValue().getVisitedStations().stream()
                        .noneMatch(stationState -> stationState.getData().getLocation().getLatLng().equals(stationLocation))) // Check if station has not been visited
                .map(entry -> sessionMap.get(entry.getKey())) // Retrieve WebSocketSession from sessionMap using sessionId
                .filter(Objects::nonNull) // Ensure session is not null
                .collect(Collectors.toList()); // Collect the valid WebSocket sessions
    }



}
