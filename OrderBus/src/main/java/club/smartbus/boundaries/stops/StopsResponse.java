package club.smartbus.boundaries.stops;

import club.smartbus.boundaries.routes.RouteResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopsResponse {
    private RouteResponse routeResponse;
}
