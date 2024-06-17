package org.example.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.util.annotation.NonNull;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopId implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    @NonNull private String lineNumber;
    @NonNull private Integer stopName;
}
