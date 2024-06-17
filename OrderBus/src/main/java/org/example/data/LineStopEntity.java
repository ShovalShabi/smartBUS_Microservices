package org.example.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@IdClass(StopId.class)
@Table("line_stops")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineStopEntity {
    @Id @Column("line_number")  private String lineNumber;     // PK line number of the bus
    @Id @Column("stop_name")    private String stopName;       // PK stop name of the bus
        @Column("stop_order")   private Integer stopOrder;     // Order of the stop in the sequence
        @Column("lat")          private Double lat;            // Latitude of the stop
        @Column("lng")          private Double lng;            // Longitude of the stop
}
