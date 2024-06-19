package org.example.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.example.utils.Constants.ROUTES_SERVICE_URL;

@Configuration
public class WebClientConfiguration {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl(ROUTES_SERVICE_URL);
    }
}
