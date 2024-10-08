package club.smartbus.service.websocket;

import club.smartbus.boundaries.websocket.messages.WebSocketMessageBoundary;
import club.smartbus.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Component
public class WebSocketSessionManager {

    private final RedisTemplate<String, WebSocketSession> redisTemplate;

    @Autowired
    public WebSocketSessionManager(RedisTemplate<String, WebSocketSession> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Add and open a session to the appropriate Redis map based on clientType
    public void connectAndAddSession(WebSocketSession session) {
        if (session != null && session.isOpen()) {
            redisTemplate.opsForValue().set(session.getId(), session);
            log.info("Session {} connected and added to Redis", session.getId());
        } else {
            log.warn("Attempted to add a session that is null or not open");
        }
    }

    // Close and remove a session from the appropriate Redis map based on clientType
    public void closeAndRemoveSession(WebSocketSession session) {
        if (session != null) {
            try {
                session.close(); // Close the session
                redisTemplate.delete(session.getId());
                log.info("Session {} closed and removed from Redis", session.getId());
            } catch (IOException e) {
                log.error("Failed to close session {}: {}", session.getId(), e.getMessage());
            }
        } else {
            log.warn("Attempted to close and remove a null session");
        }
    }

    // Send a message to a specific session by session ID with retry logic
    public void sendMessage(String sessionId, WebSocketMessageBoundary messageBoundary) {
        WebSocketSession session = redisTemplate.opsForValue().get(sessionId);
        if (session != null && session.isOpen()) {
            boolean success = sendMessageWithRetry(session, messageBoundary);
            if (!success) {
                log.error("Failed to send message to session {} after {} attempts", sessionId, Constants.MAX_RETRIES);
                closeAndRemoveSession(session); // Assuming OrderBus client type here, adjust as necessary
            }
        } else {
            log.warn("Session {} is not open or does not exist", sessionId);
        }
    }

    // Helper method to handle retry logic
    private boolean sendMessageWithRetry(WebSocketSession session, WebSocketMessageBoundary message) {
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
}
