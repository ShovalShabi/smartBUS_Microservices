package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransitDetails {
    private StopDetails stopDetails;
    private String headsign;
    private TransitLine transitLine;
}
