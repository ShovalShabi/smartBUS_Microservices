package org.example.boundaries.stops;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.boundaries.routes.RouteResponse;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopsResponse {
    private RouteResponse routeResponse;
}
