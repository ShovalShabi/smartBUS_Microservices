package org.example.api.apiConstructionPipelines;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateDirectionRequestBody implements Stage<List<String>, String> {
    /***
     * This method creates a JSON request body for the Directions API
     * @param addresses A list of two addresses: origin and destination
     * @return A JSON string representing the request body
     */
    @Override
    public String process(List<String> addresses) {
        // Extract origin and destination addresses from the input list
        String originAddress = addresses.get(0);
        String destinationAddress = addresses.get(1);
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
        requestBody.put("languageCode", "en-US");

        try {
            // Convert the request body to a JSON string
            return objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("Error converting request body to JSON", e);
        }
    }
}
