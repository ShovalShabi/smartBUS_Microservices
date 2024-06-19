package org.example.boundaries.stops;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.boundaries.routes.RouteRequest;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopsRequest {
    private Boolean orderBus;
    private RouteRequest routeRequest;
}
