package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vehicle {
    private VehicleName name;
    private String type;
    private String iconUri;
}
