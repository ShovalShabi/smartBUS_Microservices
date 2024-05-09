package org.example.dto.polyline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LatLng {
    private Double latitude;
    private Double longitude;

    public LatLng(com.google.maps.model.LatLng latLng) {
        this.latitude = latLng.lat;
        this.longitude = latLng.lng;
    }
}
