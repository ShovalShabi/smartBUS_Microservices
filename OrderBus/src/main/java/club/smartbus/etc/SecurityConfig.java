package club.smartbus.etc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration class to handle Spring Security settings.
 * This class configures security rules for various endpoints including WebSocket connections.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for WebSocket endpoints or customize its behavior for WebSockets
                .csrf(AbstractHttpConfigurer::disable)

                // Configure authorization rules for different endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/notification-service/**").permitAll()  // Allow WebSocket connections without authentication
                        .requestMatchers("/bus/**").permitAll()  // Allow public access to /bus/ endpoints
                        .anyRequest().authenticated()  // All other requests need authentication
                )
                // Enable basic authentication using the new lambda style
                .httpBasic(withDefaults());  // Replaces the old `httpBasic()` method

        return http.build();
    }
}
