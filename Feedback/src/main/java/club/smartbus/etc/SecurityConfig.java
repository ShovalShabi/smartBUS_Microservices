package club.smartbus.etc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration class for the WebFlux application.
 *
 * <p>This class enables security settings for the application using Spring Security in a reactive environment.
 * It configures basic authentication, form-based login, and disables CSRF protection for development purposes.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.config.client.orderbus}")
    private String orderBusClientOrigin;

    /**
     * Configures the security filter chain for the WebFlux application.
     *
     * <p>This method defines the security rules for different endpoints, enabling basic and form-based authentication
     * while disabling CSRF protection. It also allows unrestricted access to Swagger-UI endpoints and requires authentication
     * for all other routes.
     *
     * @param http the {@link ServerHttpSecurity} object that provides a fluent API for security configuration
     * @return a {@link SecurityWebFilterChain} defining the security configuration for the application
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // Disable CSRF protection for this configuration
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // Configure CORS settings
                .cors(corsSpec -> corsSpec.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    // Allow requests from the specified frontend origins (localhost on ports 6001 and 7001)
                    config.setAllowedOrigins(List.of(orderBusClientOrigin));
                    // Allow the following HTTP methods: GET, POST, PUT, DELETE, and OPTIONS
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    // Allow all headers in the request (no restriction on headers)
                    config.setAllowedHeaders(List.of("*"));
                    // Allow credentials such as cookies and tokens to be sent in cross-origin requests
                    config.setAllowCredentials(true);
                    // Return the CORS configuration to be applied
                    return config;
                }))

                // Allow unrestricted access to all endpoints (no authentication required for any endpoint)
                .authorizeExchange(authorize -> authorize
                        .anyExchange().permitAll()  // Permit all requests to all endpoints without authentication
                )
                // Disable HTTP Basic Authentication (no username/password prompt for HTTP requests)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                // Disable form-based login (no login page for browser-based requests)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // Build the security configuration and apply it
                .build();
    }
}
