package club.smartbus.boundaries.routes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RouteRequest {
    private String originAddress;
    private String destinationAddress;
}
