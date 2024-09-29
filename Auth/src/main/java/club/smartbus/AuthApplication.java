package club.smartbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * The entry point of the SmartBus Authentication Application.
 * <p>
 * This class configures and starts the Spring Boot application.
 * It also enables specific modules such as Reactive Web (WebFlux) and
 * R2DBC repositories for non-blocking database access.
 * </p>
 * <ul>
 *     <li>{@link SpringBootApplication} - Configures the Spring Boot application, enabling auto-configuration and component scanning.</li>
 *     <li>{@link ConfigurationPropertiesScan} - Enables scanning for configuration properties beans.</li>
 *     <li>{@link EnableR2dbcRepositories} - Enables R2DBC repositories to handle reactive database operations.</li>
 *     <li>{@link EnableWebFlux} - Enables the WebFlux framework for building reactive web applications.</li>
 * </ul>
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableR2dbcRepositories
@EnableWebFlux
public class AuthApplication {

    /**
     * The main method that acts as the entry point for the Spring Boot application.
     * <p>
     * This method launches the SmartBus Authentication Application using the embedded Tomcat server.
     * </p>
     *
     * @param args Command line arguments passed to the application (typically not used).
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}