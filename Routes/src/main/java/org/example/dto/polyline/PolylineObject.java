package org.example.dto.polyline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PolylineObject {
    private List<PolylineWrapper> routes;
}
