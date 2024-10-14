package club.smartbus.api;

import club.smartbus.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up WebClient and CORS (Cross-Origin Resource Sharing).
 * This class handles the WebClient configuration for external API communication and configures
 * CORS policies to allow communication between different domains.
 */
@Configuration
public class WebClientConfiguration {

    /**
     * The base URL for the OrderBus client, injected from the application properties.
     * This value is specified in the application's configuration files (application.properties or application.yml).
     */
    @Value("${client.orderbus}")
    private String orderBusUrl;

    /**
     * Creates and configures a {@link WebClient.Builder} bean with a base URL for the Routes service.
     * The base URL is taken from {@link Constants#ROUTES_SERVICE_URL}.
     *
     * @return a {@link WebClient.Builder} configured with the Routes service base URL.
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl(Constants.ROUTES_SERVICE_URL);
    }

    /**
     * Configures CORS policies to allow HTTP requests from other domains, especially for external clients.
     * The CORS policy is set to allow requests from the domain specified by the `client.orderbus` property.
     *
     * The allowed methods include:
     * <ul>
     *   <li>GET</li>
     *   <li>POST</li>
     *   <li>PUT</li>
     *   <li>DELETE</li>
     *   <li>OPTIONS</li>
     * </ul>
     * It also allows all headers and supports credentials.
     *
     * @return a {@link WebMvcConfigurer} that defines the CORS configuration for the application.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(orderBusUrl) // Later on, more clients can be added
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
