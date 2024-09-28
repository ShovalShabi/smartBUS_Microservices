package club.smartbus.etc;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Swagger configuration class for enabling API documentation.
 *
 * <p>This class configures and provides the OpenAPI specification for the feedback service.
 * It is activated only when the application is running under the "dev" or "test" profiles.
 * Swagger is used to provide interactive API documentation via the Swagger-UI interface.
 */
@Configuration
@Profile({"dev", "test"})  // Enable Swagger-UI only for dev and test profiles
public class SwaggerConfig {

    /**
     * Configures the OpenAPI specification for the feedback service.
     *
     * <p>This method sets up an {@link OpenAPI} bean, which provides metadata such as the title
     * and version of the API. This information is used by Swagger-UI to generate an interactive
     * API documentation page.
     *
     * @return an {@link OpenAPI} object containing the API metadata
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Feedback Service")  // Set the title of the API
                        .version("v1.0"));  // Set the version of the API
    }
}
