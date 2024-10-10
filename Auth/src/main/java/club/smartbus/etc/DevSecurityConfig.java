package club.smartbus.etc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration class that defines the security settings for development and test environments.
 * <p>
 * This configuration is only active when the application is running in either the "dev" or "test"
 * profiles, as specified by the {@link Profile} annotation. It allows all incoming requests to bypass
 * security restrictions, which is useful for development and testing purposes.
 * </p>
 */
@Configuration
@Profile({"dev", "test"})  // Apply only to dev and test environments
public class DevSecurityConfig {

    /**
     * Configures the security filter chain to disable CSRF protection and allow all requests without
     * authentication.
     * <p>
     * This method creates a {@link SecurityWebFilterChain} bean that disables CSRF protection and
     * allows all HTTP requests in development and test environments. The purpose is to simplify
     * development by removing authentication requirements and ensuring all endpoints are accessible.
     * </p>
     *
     * @param http the {@link ServerHttpSecurity} object used to configure web security.
     * @return a {@link SecurityWebFilterChain} that defines the security rules for the application.
     */
    @Bean(name = "devSecurityWebFilterChain")  // Renamed bean to avoid conflict
    public SecurityWebFilterChain devSecurityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF for development purposes
                .cors(withDefaults()) // Make sure CORS is enabled
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/**").permitAll()  // Allow all requests without authentication in dev/test
                )
                .build();
    }
}
