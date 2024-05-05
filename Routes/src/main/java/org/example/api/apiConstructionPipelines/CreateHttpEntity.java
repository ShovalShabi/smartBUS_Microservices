package org.example.api.apiConstructionPipelines;

import org.example.api.ApiHeadersProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreateHttpEntity implements Stage<String, HttpEntity<String>> {
    @Autowired
    private ApiHeadersProperties apiHeaders;

    /***
     * Process the body of the request
     * @param body The body of the request
     * @return The HttpEntity object
     */
    @Override
    public HttpEntity<String> process(String body) {
        // Set headers for the API request and make the request to the API URL to get the JSON response
        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> headers = apiHeaders.getHeaders();
        httpHeaders.setAll(headers);
        return new HttpEntity<String>(body, httpHeaders);
    }
}
