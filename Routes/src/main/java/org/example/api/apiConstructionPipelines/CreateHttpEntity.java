package org.example.api.apiConstructionPipelines;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Map;

public class CreateHttpEntity implements Stage<String, HttpEntity<String>> {
    @Value("${api.headers}")
    private Map<String, String> apiHeaders;

    /***
     * Process the body of the request
     * @param body The body of the request
     * @return The HttpEntity object
     */
    @Override
    public HttpEntity<String> process(String body) {
        // Set headers for the API request and make the request to the API URL to get the JSON response
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(apiHeaders);
        return new HttpEntity<String>(body, httpHeaders);
    }
}
