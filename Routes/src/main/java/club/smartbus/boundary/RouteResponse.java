package club.smartbus.boundary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RouteResponse {
    private String origin;
    private String destination;
    private String initialDepartureTime;
    private String finalArrivalTime;
    private String publishedTimestamp;
    private Map<String, Object> routeFlow;
}
