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

@EnableAutoConfiguration
@Table("feedback")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("rating")
    private Double rating;

    @Column("agency")
    private String agency;

    @Column("line_number")
    private String lineNumber;

    @Column("user_email")
    private String userEmail;

    @Column("additional_details")
    private String additionalDetails;

    @Column("creation_timestamp")
    private LocalDateTime creationTimestamp;


    public FeedbackEntity(FeedbackDTO feedbackDTO) {
        //letting the database create the uuid
        rating = feedbackDTO.getRating();
        agency = feedbackDTO.getAgency();
        lineNumber = feedbackDTO.getLineNumber();
        userEmail = feedbackDTO.getUserEmail();
        additionalDetails = feedbackDTO.getAdditionalDetails();
        creationTimestamp = LocalDateTime.now();
    }

    public FeedbackDTO toDTO() {
        return new FeedbackDTO(rating, agency, lineNumber, userEmail, additionalDetails, creationTimestamp);
    }
}
