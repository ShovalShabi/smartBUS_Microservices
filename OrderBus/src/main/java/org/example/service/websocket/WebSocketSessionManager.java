package org.example.service.websocket;

import lombok.extern.slf4j.Slf4j;
import org.example.boundaries.websocket.messages.WebSocketMessageBoundary;
import org.example.utils.Location;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.utils.Constants.*;

@Slf4j
@Component
public class WebSocketSessionManager {

    // Separate maps for OrderBus and DriverConsole sessions
    private final ConcurrentHashMap<String, WebSocketSession> orderBusSessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, WebSocketSession> driverConsoleSessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Location, List<String>> sessionIDListenersToLocation = new ConcurrentHashMap<>();   //Each session id is mapped to the location the which the location is needed to pick passengers


    // Add and open a session to the appropriate map based on clientType
    public void connectAndAddSession(WebSocketSession session, String clientType) {
        if (session != null && session.isOpen()) {
            switch (clientType) {
                case ORDER_BUS_CLIENT -> {
                    orderBusSessions.put(session.getId(),session);
                }
                case DRIVER_CONSOLE_CLIENT -> {
                    driverConsoleSessions.put(session.getId(),session);
                }
            }
            log.info("Session {} connected and added to {} sessions", session.getId(), clientType);
        } else {
            log.warn("Attempted to add a session that is null or not open");
        }
    }

    // Close and remove a session from the appropriate map based on clientType
    public void closeAndRemoveSession(WebSocketSession session, String clientType) {
        if (session != null) {
            try {
                session.close(); // Close the session
                switch (clientType) {
                    case ORDER_BUS_CLIENT -> {
                        orderBusSessions.remove(session.getId());
                    }
                    case DRIVER_CONSOLE_CLIENT -> {
                        driverConsoleSessions.remove(session.getId());
                    }
                }
                log.info("Session {} closed and removed from {} sessions", session.getId(), clientType);
            } catch (IOException e) {
                log.error("Failed to close session {}: {}", session.getId(), e.getMessage());
            }
        } else {
            log.warn("Attempted to close and remove a null session");
        }
    }

    // Send a message to a specific session by session ID with retry logic (you can add clientType handling here if needed)
    public void sendMessage(String sessionId, WebSocketMessageBoundary messageBoundary) {
        WebSocketSession session = orderBusSessions.get(sessionId);
        if (session == null) {
            session = driverConsoleSessions.get(sessionId);
        }
        if (session != null && session.isOpen()) {
            boolean success = sendMessageWithRetry(session, messageBoundary, 0);
            if (!success) {
                log.error("Failed to send message to session {} after {} attempts", sessionId, MAX_RETRIES);
                closeAndRemoveSession(session, "OrderBus"); // Assuming OrderBus client type here, adjust as necessary
            }
        } else {
            log.warn("Session {} is not open or does not exist", sessionId);
        }
    }

    // Broadcast a message to all sessions with retry logic
    public void broadcastMessage(WebSocketMessageBoundary messageBoundary) {
        orderBusSessions.values().forEach(session -> sendMessageWithRetry(session, messageBoundary, 0));
        driverConsoleSessions.values().forEach(session -> sendMessageWithRetry(session, messageBoundary, 0));
    }

    // Helper method to handle retry logic
    private boolean sendMessageWithRetry(WebSocketSession session, WebSocketMessageBoundary message, int attempt) {
        while (attempt < MAX_RETRIES - 1) {
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
}
