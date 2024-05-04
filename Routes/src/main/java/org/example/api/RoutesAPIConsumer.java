package org.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.api.apiConstructionPipelines.Pipeline;
import org.example.dto.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
@ConfigurationProperties(prefix = "api")
public class RoutesAPIConsumer {
    private final RestTemplateBuilder restTemplateBuilder;
    private Pipeline createRequestBodyPipeline;
    @Value("${api.url}")
    private String apiUrl;

    @Autowired
    private RoutesAPIConsumer(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @PostConstruct
    public void init() {
        createRequestBodyPipeline = new Pipeline();
    }

    /***
     * Get routes json from the Directions API
     * @param originAddress The origin address
     * @param destinationAddress The destination address
     * @return The Route object containing the routes
     * @throws RestClientException If there is an error getting the routes json
     */
    public Route getRoutesFromAPI(String originAddress, String destinationAddress) throws RestClientException {
        // Create the request body for the Directions API
        try {
            HttpEntity<String> entity = createRequestBodyPipeline.execute(List.of(originAddress, destinationAddress));
            String stringMap = restTemplateBuilder.build().postForObject(apiUrl, entity, String.class);
            return new ObjectMapper().readValue(stringMap, Route.class);
        } catch (Exception e) {
            throw new RuntimeException("Error getting routes json", e);
        }
    }
}