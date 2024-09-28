package club.smartbus.etc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

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
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF for development purposes
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // Allow access to Swagger-UI endpoints without authentication
                        .anyExchange().authenticated()  // Require authentication for all other endpoints
                )
                .httpBasic(withDefaults())  // Enable basic authentication for HTTP requests
                .formLogin(withDefaults())  // Enable form-based login for browser-based requests
                .build();
    }
}
