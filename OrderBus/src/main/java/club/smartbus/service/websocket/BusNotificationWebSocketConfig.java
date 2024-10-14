package club.smartbus.service.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configuration class that enables and sets up WebSocket communication for bus notifications.
 * This class implements {@link WebSocketConfigurer} and registers WebSocket handlers to handle
 * communication between the client and the notification service.
 */
@Configuration
@EnableWebSocket
public class BusNotificationWebSocketConfig implements WebSocketConfigurer {

    /**
     * WebSocket handler responsible for managing the WebSocket connections and messages related to bus notifications.
     */
    private final BusWebSocketHandler busWebSocketHandler;

    /**
     * Constructor that injects the {@link BusWebSocketHandler} used for handling WebSocket messages.
     *
     * @param busWebSocketHandler the WebSocket handler responsible for managing notification-related WebSocket connections.
     */
    @Autowired
    public BusNotificationWebSocketConfig(BusWebSocketHandler busWebSocketHandler) {
        this.busWebSocketHandler = busWebSocketHandler;
    }

    /**
     * Registers WebSocket handlers for specific endpoints.
     * This method maps the handler for the "/notification-service" endpoint, allowing any origin to connect.
     *
     * @param registry the {@link WebSocketHandlerRegistry} used to register WebSocket handlers.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(busWebSocketHandler, "/notification-service")
                .setAllowedOriginPatterns("*");
    }
}
