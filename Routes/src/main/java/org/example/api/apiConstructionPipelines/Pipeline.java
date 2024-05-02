package org.example.api.apiConstructionPipelines;

import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {
    private final Stage<List<String>, String> createBodyStage = new CreateDirectionRequestBody();
    private final Stage<String, HttpEntity<String>> createHttpEntity = new CreateHttpEntity();
    public HttpEntity<String> execute(List<String> addresses) throws Exception {
        try {
            String body = createBodyStage.process(addresses);
            return createHttpEntity.process(body);
        } catch (Exception e) {
            throw new Exception("Error executing pipeline", e);
        }
    }
}
