package club.smartbus.boundaries.stops;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopBoundary {
    private String lineNumber;
    private String stopName;
    private Integer stopOrder;
    private Double lat;
    private Double lng;
}
