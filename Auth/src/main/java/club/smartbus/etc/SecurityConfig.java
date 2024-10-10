package club.smartbus.etc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration class that manages user authentication, authorization, and HTTP security rules.
 * <p>
 * This class sets up in-memory user authentication and configures security filters for the application.
 * It also disables CSRF protection for development purposes and sets up basic and form-based authentication.
 * </p>
 */
@Slf4j
@Configuration
public class SecurityConfig {

    private final StartupConfig startupConfig;

    /**
     * Constructor for SecurityConfig that injects {@link StartupConfig}.
     *
     * @param startupConfig The startup configuration that provides the admin user's encoded password.
     */
    public SecurityConfig(StartupConfig startupConfig) {
        this.startupConfig = startupConfig;
    }

    /**
     * Configures an in-memory user details service for authentication.
     * <p>
     * This method retrieves the encoded admin password from the {@link StartupConfig} and sets up an
     * in-memory user with the "ADMIN" role. If the password is not initialized, it throws an
     * {@link IllegalStateException}.
     * </p>
     *
     * @return a {@link InMemoryUserDetailsManager} containing the admin user details.
     * @throws IllegalStateException if the encoded admin password has not been initialized.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        String encodedPassword = startupConfig.getEncodedPassword();

        if (encodedPassword == null) {
            throw new IllegalStateException("Encoded password has not been initialized");
        }

        UserDetails admin = User.builder()
                .username("admin")
                .password(encodedPassword)  // Use encoded password here
                .roles("ADMIN")
                .build();

        log.info("Setting up admin user with encoded password.");
        return new InMemoryUserDetailsManager(admin);
    }

    /**
     * Configures security rules for the application using {@link ServerHttpSecurity}.
     * <p>
     * This method disables CSRF protection for development environments and permits access to Swagger
     * and API documentation paths without authentication. All other requests are authenticated using
     * either basic authentication or form-based login.
     * </p>
     *
     * @param http The {@link ServerHttpSecurity} object used to configure security settings.
     * @return a {@link SecurityWebFilterChain} that defines the security configuration for web requests.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF for development purposes
                .cors(withDefaults()) // Make sure CORS is enabled
                .authorizeExchange(authorize -> authorize
                        // Disable security for Swagger and API documentation endpoints
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        // Permit all requests in non-production environments (e.g., dev and test)
                        .pathMatchers("/**").permitAll()
                        .anyExchange().authenticated()  // Require authentication for all other endpoints
                )
                .httpBasic(withDefaults())  // Enable basic authentication for HTTP requests
                .formLogin(withDefaults())  // Enable form-based login for browser-based requests
                .build();
    }
}
