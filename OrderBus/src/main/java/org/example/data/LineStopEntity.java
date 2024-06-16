package org.example.data;

import jakarta.persistence.EmbeddedId;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("line_stops")
public class LineStopEntity {
    @EmbeddedId
    private StopId id;

    @Column("line_number")
    private String lineNumber;

    @Column("stop_name")
    private String stopName;

    @Column("stop_order")
    private Integer stopOrder;

    @Column("lat")
    private Double lat;

    @Column("lng")
    private Double lng;
}
