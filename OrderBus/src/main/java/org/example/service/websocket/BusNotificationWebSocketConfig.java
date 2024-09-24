package org.example.service.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSocket
public class BusNotificationWebSocketConfig implements WebSocketConfigurer {

    private final BusWebSocketHandler busWebSocketHandler;

    @Autowired
    public BusNotificationWebSocketConfig(BusWebSocketHandler busWebSocketHandler) {
        this.busWebSocketHandler = busWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(busWebSocketHandler, "/notification-service")
                .setAllowedOriginPatterns("*");
    }
}
