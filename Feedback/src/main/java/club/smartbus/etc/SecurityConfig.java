package club.smartbus.etc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF for development
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // Allow access to Swagger-UI
                        .anyExchange().authenticated()  // All other requests need authentication
                )
                .httpBasic(withDefaults())  // Enable basic authentication
                .formLogin(withDefaults())  // Enable form-based login
                .build();
    }
}



