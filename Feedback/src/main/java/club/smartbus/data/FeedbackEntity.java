package club.smartbus.data;

import club.smartbus.dto.FeedbackDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The {@code FeedbackEntity} class represents the feedback records stored in the database.
 *
 * <p>It is mapped to the "feedback" table in the database and contains various fields that represent
 * feedback information such as the rating, the company (agency), the bus line, the user who provided the feedback,
 * additional feedback details, and the timestamp of when the feedback was created.
 *
 * <p>This class also provides utility methods for converting between {@code FeedbackEntity} and {@code FeedbackDTO}.
 */
@EnableAutoConfiguration
@Table("feedback")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackEntity {

    /**
     * Unique identifier for the feedback. It is generated using UUID and serves as the primary key in the database.
     */
    @Id
    @Column("id")
    private UUID id;

    /**
     * Rating provided in the feedback. It represents the quality of service rated by the user.
     */
    @Column("rating")
    private Double rating;

    /**
     * The name of the company or agency for which the feedback is given.
     */
    @Column("agency")
    private String agency;

    /**
     * The bus line number associated with the feedback.
     */
    @Column("line_number")
    private String lineNumber;

    /**
     * The email of the user who provided the feedback.
     */
    @Column("user_email")
    private String userEmail;

    /**
     * Any additional details provided by the user in the feedback.
     */
    @Column("additional_details")
    private String additionalDetails;

    /**
     * The timestamp when the feedback was created. Automatically set to the current time when the feedback is recorded.
     */
    @Column("creation_timestamp")
    private LocalDateTime creationTimestamp;

    /**
     * Constructs a new {@code FeedbackEntity} from a {@code FeedbackDTO}. The {@code creationTimestamp} is set to the
     * current time, while other fields are copied from the provided {@code FeedbackDTO}.
     *
     * @param feedbackDTO the {@code FeedbackDTO} containing the feedback information.
     */
    public FeedbackEntity(FeedbackDTO feedbackDTO) {
        //letting the database create the uuid
        rating = feedbackDTO.getRating();
        agency = feedbackDTO.getAgency();
        lineNumber = feedbackDTO.getLineNumber();
        userEmail = feedbackDTO.getUserEmail();
        additionalDetails = feedbackDTO.getAdditionalDetails();
        creationTimestamp = LocalDateTime.now();
    }

    /**
     * Converts this {@code FeedbackEntity} to a {@code FeedbackDTO}.
     *
     * @return a {@code FeedbackDTO} containing the feedback information from this entity.
     */
    public FeedbackDTO toDTO() {
        return new FeedbackDTO(rating, agency, lineNumber, userEmail, additionalDetails, creationTimestamp);
    }
}
