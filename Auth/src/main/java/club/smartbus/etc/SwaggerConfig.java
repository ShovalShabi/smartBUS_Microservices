package club.smartbus.etc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration class responsible for setting up Swagger-UI for API documentation in development and test environments.
 * <p>
 * This class defines the OpenAPI configuration, including basic authentication as the security scheme.
 * </p>
 */
@Configuration
@Profile({"dev", "test"})  // Enable Swagger-UI only for dev and test profiles
public class SwaggerConfig {

    /**
     * Creates and configures an {@link OpenAPI} bean for Swagger-UI integration.
     * <p>
     * This method sets up the API title, version, and security requirements (Basic Authentication). The API documentation
     * is only enabled for development and test environments due to the applied {@link Profile}.
     * </p>
     *
     * @return the configured {@link OpenAPI} instance.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Auth Service")  // Set the title of the API
                        .version("v1.0"))      // Set the version of the API
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))  // Add Security Requirement
                .components(new Components()
                        .addSecuritySchemes("basicAuth",  // Define Basic Auth Security Scheme
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                        )
                );
    }
}
