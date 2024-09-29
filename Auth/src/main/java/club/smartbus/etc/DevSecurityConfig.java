package club.smartbus.etc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@Profile({"dev", "test"})  // Apply only to dev and test environments
public class DevSecurityConfig {

    @Bean(name = "devSecurityWebFilterChain")  // Renamed bean to avoid conflict
    public SecurityWebFilterChain devSecurityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF for development purposes
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/**").permitAll()  // Allow all requests without authentication in dev/test
                )
                .build();
    }
}
