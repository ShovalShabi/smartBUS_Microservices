package club.smartbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * The entry point for the Feedback application.
 *
 * <p>This is the main class that boots up the Spring Boot application. It also enables specific configurations like
 * R2DBC repositories and Spring WebFlux for reactive programming support.
 */
@SpringBootApplication
@EnableR2dbcRepositories
@EnableWebFlux
public class FeedbackApplication {

    /**
     * The main method that serves as the entry point for the Spring Boot application.
     *
     * <p>This method calls {@link SpringApplication#run(Class, String...)} to bootstrap the application,
     * starting the embedded server and initializing the Spring context.
     *
     * @param args the command-line arguments passed during the application startup
     */
    public static void main(String[] args) {
        SpringApplication.run(FeedbackApplication.class, args);
    }
}
