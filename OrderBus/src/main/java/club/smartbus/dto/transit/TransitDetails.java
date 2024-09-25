package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransitDetails {
    private String arrivalTime;
    private String departureTime;
    private String agency;
    private String lineNumber;
    private Integer stopCounts;
    private StopDetails stopDetails;
}


