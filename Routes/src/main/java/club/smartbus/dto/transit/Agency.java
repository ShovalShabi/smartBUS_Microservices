package club.smartbus.dto.transit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Agency {
    private String name;
    private String phoneNumber;
    private String uri;

}