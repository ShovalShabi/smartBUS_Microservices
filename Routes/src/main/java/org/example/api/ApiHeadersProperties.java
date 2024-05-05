package org.example.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties("api.headers")
public class ApiHeadersProperties {
    private String contentType;
    private String xGoogFieldMask;
    private String xGoogApiKey;

    public Map<String, String> getHeaders() {
        return Map.of(
                "Content-Type", contentType,
                "X-Goog-FieldMask", xGoogFieldMask,
                "X-Goog-Api-Key", xGoogApiKey);
    }
}
