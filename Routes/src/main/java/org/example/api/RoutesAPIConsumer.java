package org.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.api.apiConstructionPipelines.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

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

    public String getRoutesJsonFromApi(String originAddress, String destinationAddress) throws RestClientException {
        // Create the request body for the Directions API
        try {
            HttpEntity<String> entity = createRequestBodyPipeline.execute(List.of(originAddress, destinationAddress));
            return restTemplateBuilder.build().postForObject(apiUrl, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Error getting routes json", e);
        }
    }
}