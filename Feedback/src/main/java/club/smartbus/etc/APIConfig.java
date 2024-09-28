package club.smartbus.etc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Configuration class for enabling Cross-Origin Resource Sharing (CORS) in the WebFlux application.
 *
 * <p>The {@code APIConfig} class implements {@link WebFluxConfigurer} and overrides the
 * {@link WebFluxConfigurer#addCorsMappings(CorsRegistry)} method to configure CORS settings
 * for the application. This ensures that web clients from different origins can access the APIs
 * according to the specified rules.
 */
@Configuration
public class APIConfig implements WebFluxConfigurer {

    /**
     * Configures CORS mappings for the application.
     *
     * <p>This method is used to define the rules for handling cross-origin requests. It allows any origin
     * to access the application, as well as specific HTTP methods, headers, and credentials.
     *
     * @param registry the {@link CorsRegistry} object used to register the CORS configuration
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow CORS requests for all endpoints
                .allowedOriginPatterns("*") // Allow all origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials (e.g., cookies, authorization headers)
    }
}
