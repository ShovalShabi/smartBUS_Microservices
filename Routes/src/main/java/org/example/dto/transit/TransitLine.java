package org.example.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransitLine {
    private List<Agency> agencies;
    private String name;
    private String color;
    private String nameShort;
    private String textColor;
    private Vehicle vehicle;
}
