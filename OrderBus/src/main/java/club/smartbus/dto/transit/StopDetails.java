package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StopDetails {
    private Stop arrivalStop;
    private Stop departureStop;
    private String arrivalTime;
    private String departureTime;
    private Optional<List<Station>> stations;
}
