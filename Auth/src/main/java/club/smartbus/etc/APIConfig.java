package club.smartbus.etc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Configuration class to define CORS (Cross-Origin Resource Sharing) settings for the application.
 * <p>
 * This class implements {@link WebFluxConfigurer} and allows the application to handle CORS requests
 * globally by setting the appropriate CORS policies for all endpoints.
 * </p>
 */
@Configuration
public class APIConfig implements WebFluxConfigurer {

    /**
     * Configure CORS mapping to allow cross-origin requests.
     * <p>
     * This method overrides the default behavior and allows the application to accept cross-origin
     * requests from any origin, for specific HTTP methods, headers, and credentials. It is useful
     * in development environments and scenarios where the frontend and backend may be hosted on
     * different domains or ports.
     * </p>
     *
     * @param registry The CORS registry that provides the CORS mapping configuration.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow CORS requests for all endpoints
                .allowedOriginPatterns("*") // Allow all origins (wildcard)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials (cookies, authorization headers, etc.)
    }
}
