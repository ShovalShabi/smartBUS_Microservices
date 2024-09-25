package club.smartbus.gateway;

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

//    @Bean
//    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .routes(route -> routeConfig.getRoutes().stream()
//                        .map(route -> route.route(r ->
//                                r.path(route.getPath())
//                                        .filters(f -> f.rewritePath(route.getPath(), "/"))
//                                        .uri(route.getUrl())
//                                        .predicate(this::checkOrigin)
//                        )).toArray()
//                )
//                .build();
//    }
//
//    private Mono<Boolean> checkOrigin(ServerWebExchange exchange) {
//        // Get the request's origin URL
//        String origin = exchange.getRequest().getHeaders().getOrigin();
//
//        // Check if the origin matches the allowed URL
//        boolean isAllowedOrigin = "http://192.0.0.1:5000".equals(origin);
//
//        // Return a Mono indicating whether the request is allowed from the specified origin
//        return Mono.just(isAllowedOrigin);
//    }
}
