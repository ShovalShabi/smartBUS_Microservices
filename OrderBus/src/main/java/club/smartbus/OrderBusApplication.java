package club.smartbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Order Bus Application.
 * This class is responsible for bootstrapping and launching the Spring Boot application.
 *
 * <p>The application manages the ordering of buses for passengers and integrates various microservices
 * for bus routing, stops, WebSocket communication, and more.</p>
 */
@SpringBootApplication
public class OrderBusApplication {

    /**
     * The main method used to launch the Spring Boot application.
     *
     * <p>This method calls {@link SpringApplication#run(Class, String...)} to start the Spring Boot application,
     * initializing all configured components and services.</p>
     *
     * @param args Command-line arguments that can be passed to customize the application's startup behavior.
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderBusApplication.class, args);
    }
}
