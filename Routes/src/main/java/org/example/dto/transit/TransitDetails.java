package org.example.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransitDetails {
    private StopDetails stopDetails;
    private LocalizedValues localizedValues;
    private String headsign;
    private TransitLine transitLine;
    private int stopCount;
}
