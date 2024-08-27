package org.example.service.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.boundaries.websocket.messages.WebSocketMessageBoundary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class BusWebSocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper;
    private final WebSocketSessionManager sessionManager;

    @Autowired
    public BusWebSocketHandler(ObjectMapper objectMapper, WebSocketSessionManager sessionManager) {
        this.objectMapper = objectMapper;
        this.sessionManager = sessionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            sessionManager.connectAndAddSession(session);
            log.info("WebSocket connection {} established", session.getId());
        } catch (Exception e) {
            log.error("Failed to establish WebSocket connection: {}", e.getMessage());
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        try {
            WebSocketMessageBoundary messageBoundary = objectMapper.readValue(message.getPayload().toString(), WebSocketMessageBoundary.class);

            // Add handling logic based on messageBoundary
            switch (messageBoundary.getOption()) {
                case BUS_NEARBY -> {
                    // Handle BUS_NEARBY case
                }
                // Add other cases as needed
            }
        } catch (Exception e) {
            log.error("Failed to handle WebSocket message: {}", e.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        try {
            sessionManager.closeAndRemoveSession(session);
            log.error("Transport error: {}", exception.getMessage());
        } catch (Exception e) {
            log.error("Failed to handle transport error: {}", e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        try {
            sessionManager.closeAndRemoveSession(session);
            log.info("WebSocket connection {} is closed with close status: {}", session.getId(), closeStatus);
        } catch (Exception e) {
            log.error("Failed to handle connection closure: {}", e.getMessage());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private String getClientTypeFromSession(WebSocketSession session) {
        Map<String, String> queryParams = getQueryParams(session);
        return queryParams.get("clientType");
    }

    private Map<String, String> getQueryParams(WebSocketSession session) {
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        return org.springframework.web.util.UriComponentsBuilder.fromUriString("?" + query)
                .build().getQueryParams().toSingleValueMap();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        try {
            final int R = 6371000; // Radius of the earth in meters
            double latDistance = Math.toRadians(lat2 - lat1);
            double lonDistance = Math.toRadians(lon2 - lon1);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return R * c; // Distance in meters
        } catch (Exception e) {
            log.error("Failed to calculate distance: {}", e.getMessage());
            return 0;
        }
    }
}


