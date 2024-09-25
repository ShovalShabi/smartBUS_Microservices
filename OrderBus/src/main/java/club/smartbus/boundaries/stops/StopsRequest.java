package club.smartbus.boundaries.stops;

import club.smartbus.boundaries.routes.RouteRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopsRequest {
    private Boolean orderBus;
    private RouteRequest routeRequest;
}
