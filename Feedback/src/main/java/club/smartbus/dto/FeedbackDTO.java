package club.smartbus.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for transferring feedback information between client and server.
 *
 * <p>The {@code FeedbackDTO} class is responsible for holding feedback-related data.
 * It includes validation annotations to ensure that incoming data is valid and meets
 * specific requirements. The class is used both when creating new feedback and when retrieving
 * feedback from the system.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackDTO {

    /**
     * The rating provided by the user.
     *
     * <p>This field cannot be null and must be a number greater than or equal to 1.
     * A custom validation message is provided for invalid values.
     */
    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    private Double rating;

    /**
     * The agency or company for which the feedback is being provided.
     *
     * <p>This field cannot be blank. It ensures that the user provides a valid name for the agency.
     * A custom validation message is provided when the field is empty.
     */
    @NotBlank(message = "Agency cannot be empty")
    private String agency;

    /**
     * The bus line number associated with the feedback.
     *
     * <p>This field cannot be blank. A custom validation message is provided when the line number
     * is not supplied.
     */
    @NotBlank(message = "Line number cannot be empty")
    private String lineNumber;

    /**
     * The email of the user submitting the feedback.
     *
     * <p>This field is validated to ensure it contains a properly formatted email address.
     * If the format is invalid, a custom message is returned.
     */
    @Email(message = "Invalid email format")
    private String userEmail;

    /**
     * Additional comments or details provided by the user.
     *
     * <p>The size of this field is restricted to 500 characters. If the user provides more than
     * 500 characters, a custom message will indicate the issue.
     */
    @Size(max = 500, message = "Additional details cannot exceed 500 characters")
    private String additionalDetails;

    /**
     * The timestamp when the feedback was created.
     *
     * <p>This field can be automatically populated by the system to record the exact time when
     * the feedback was submitted. It is represented as a {@code LocalDateTime}.
     */
    private LocalDateTime creationTimestamp;
}
