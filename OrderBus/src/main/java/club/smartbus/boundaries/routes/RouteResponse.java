package club.smartbus.boundaries.routes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RouteResponse {
    private String origin;
    private String destination;
    private String initialDepartureTime;
    private String finalArrivalTime;
    private String publishedTimestamp;
    private Map<String, Object> routeFlow;
}
