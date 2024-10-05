package club.smartbus.boundaries.stations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StationsRequest {
    String lineNumber;
    String agency;
}
