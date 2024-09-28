package club.smartbus.etc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Test security configuration class used for disabling security during unit and integration tests.
 * This configuration allows for testing without requiring authentication or CSRF protection.
 */
@TestConfiguration
public class TestSecurityConfig {

    /**
     * Configures the security settings for testing purposes.
     * Disables CSRF protection and allows all exchanges without any authentication.
     *
     * @param http the {@link ServerHttpSecurity} instance to configure the security settings.
     * @return a {@link SecurityWebFilterChain} that permits all requests and disables CSRF protection.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF protection for testing purposes
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())  // Permit all exchanges without authentication
                .build();
    }
}
