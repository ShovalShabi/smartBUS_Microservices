package club.smartbus.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackDTO {

    @NotNull(message = "Rating cannot be null")
    @Min(value = 0, message = "Rating must be at least 0")
    private Double rating;

    @NotBlank(message = "Agency cannot be empty")
    private String agency;

    @NotBlank(message = "Line number cannot be empty")
    private String lineNumber;

    @Email(message = "Invalid email format")
    private String userEmail;

    @Size(max = 500, message = "Additional details cannot exceed 500 characters")
    private String additionalDetails;

    private LocalDateTime creationTimestamp;
}
