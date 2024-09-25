package club.smartbus.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "api.headers")
public class ApiHeadersProperties {
    // Transit API Headers
    private String TransitContentType;
    private String TransitXGoogFieldMask;
    private String TransitXGoogApiKey;

    // Polyline Google API Headers
    private String PolylineContentType;
    private String PolylineXGoogFieldMask;
    private String PolylineXGoogApiKey;

    public Map<String, String> getTransitHeaders() {
        return Map.of(
                "Content-Type", TransitContentType,
                "X-Goog-FieldMask", TransitXGoogFieldMask,
                "X-Goog-Api-Key", TransitXGoogApiKey);
    }

    public Map<String, String> getPolylineHeaders() {
        return Map.of(
                "Content-Type", PolylineContentType,
                "X-Goog-FieldMask", PolylineXGoogFieldMask,
                "X-Goog-Api-Key", PolylineXGoogApiKey);
    }
}
