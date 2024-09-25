package club.smartbus.api;

import club.smartbus.boundary.RouteRequest;
import club.smartbus.dto.polyline.PolylineObject;
import club.smartbus.dto.transit.TransitObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ConfigurationProperties(prefix = "api")
@Qualifier("ApiConsumer")
public class RoutesAPIConsumer {
    private final RestTemplateBuilder restTemplateBuilder;
    private final ApiHeadersProperties apiHeaders;
    @Value("${api.url}")
    private String apiUrl;

    private enum apiType {TRANSIT, POLYLINE}

    ;

    @Autowired
    private RoutesAPIConsumer(RestTemplateBuilder restTemplateBuilder,
                              ApiHeadersProperties apiHeaders) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.apiHeaders = apiHeaders;
    }

    /***
     * Get routes json from the Directions API
     * @param routeRequest The RouteRequest object containing the origin and destination addresses
     * @return The Route object containing the routes
     * @throws RestClientException If there is an error getting the routes json
     */
    public TransitObject getTransitFromAPI(RouteRequest routeRequest) throws RestClientException {
        // Create the request body for the Directions API
        try {
            String originAddress = routeRequest.getOriginAddress();
            String destinationAddress = routeRequest.getDestinationAddress();
            String requestBody = createRequestBody(originAddress, destinationAddress);
            HttpEntity<String> httpEntity = createHttpEntity(requestBody, apiType.TRANSIT);
            return this.restTemplateBuilder.build()
                    .postForObject(apiUrl, httpEntity, TransitObject.class);
        } catch (Exception e) {
            throw new RuntimeException("Error getting transit json", e);
        }
    }

    /***
     * Get polyline json from the Directions API
     * @param routeRequest The RouteRequest object containing the origin and destination addresses
     * @return The PolylineObject object containing the polyline
     * @throws RestClientException If there is an error getting the polyline json
     */
    public PolylineObject getPolylineFromAPI(RouteRequest routeRequest) throws RestClientException {
        // Create the request body for the Directions API
        try {
            String originAddress = routeRequest.getOriginAddress();
            String destinationAddress = routeRequest.getDestinationAddress();
            String requestBody = createRequestBody(originAddress, destinationAddress);
            HttpEntity<String> httpEntity = createHttpEntity(requestBody, apiType.POLYLINE);
            return this.restTemplateBuilder.build()
                    .postForObject(apiUrl, httpEntity, PolylineObject.class);
        } catch (Exception e) {
            throw new RuntimeException("Error getting polyline json", e);
        }
    }

    /***
     * This method creates a JSON request body for the Directions API
     * @param originAddress The origin address
     * @param destinationAddress The origin address
     * @return A JSON string representing the request body
     */
    private String createRequestBody(String originAddress, String destinationAddress) {
        // Extract origin and destination addresses from the input list
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> origin = new HashMap<>();
        origin.put("address", originAddress);

        Map<String, Object> destination = new HashMap<>();
        destination.put("address", destinationAddress);

        Map<String, Object> transitPreferences = new HashMap<>();
        transitPreferences.put("routingPreference", "LESS_WALKING");
        transitPreferences.put("allowedTravelModes", List.of("BUS")); // Use Arrays.asList for list

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("origin", origin);
        requestBody.put("destination", destination);
        requestBody.put("travelMode", "TRANSIT");
        requestBody.put("transitPreferences", transitPreferences);
        requestBody.put("computeAlternativeRoutes", true);
        requestBody.put("languageCode", "iw");

        try {
            // Convert the request body to a JSON string
            return objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("Error converting request body to JSON", e);
        }
    }

    /***
     * Process the body of the request
     * @param body The body of the request
     * @return The HttpEntity object
     */
    private HttpEntity<String> createHttpEntity(String body, apiType type) {
        // Set headers for the API request and make the request to the API URL to get the JSON response
        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> headers = (type == apiType.TRANSIT)
                ? apiHeaders.getTransitHeaders() : apiHeaders.getPolylineHeaders();
        httpHeaders.setAll(headers);
        return new HttpEntity<String>(body, httpHeaders);
    }
}