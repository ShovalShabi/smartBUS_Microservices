package org.example.data;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class StopId {
    private String lineNumber;
    private Integer stopOrder;
}
