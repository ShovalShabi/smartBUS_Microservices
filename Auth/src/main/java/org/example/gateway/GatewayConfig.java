package org.example.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    private final RouteConfig routeConfig;

    @Autowired
    public GatewayConfig(RouteConfig routeConfig) {
        this.routeConfig = routeConfig;
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        try {
            RouteLocatorBuilder.Builder routeBuilder = builder.routes();

            for (Route route : routeConfig.getRoutes()) {
                routeBuilder.route(route.getId(), r -> r
                                .path(route.getPath())
                                .uri(route.getUrl()));
            }

            return routeBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
