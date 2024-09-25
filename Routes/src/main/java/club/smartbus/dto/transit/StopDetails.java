package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopDetails {
    private Stop arrivalStop;
    private String arrivalTime;
    private Stop departureStop;
    private String departureTime;
}
