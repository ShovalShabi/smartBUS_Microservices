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

@Slf4j
@Configuration
public class SecurityConfig {

    private final StartupConfig startupConfig;

    public SecurityConfig(StartupConfig startupConfig) {
        this.startupConfig = startupConfig;
    }

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

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF for development purposes
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
