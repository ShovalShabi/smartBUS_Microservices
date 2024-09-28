package club.smartbus.service;

import club.smartbus.dto.FeedbackDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Service interface for managing feedback-related operations.
 *
 * <p>This service interface defines methods for creating feedback for a company
 * and retrieving feedback based on criteria such as rating, date range, and pagination.
 * It provides the core operations required for the feedback management functionality.
 */
public interface FeedbackService {

    /**
     * Creates feedback for a specified company.
     *
     * <p>This method allows creating a new feedback entry for the specified company.
     * The feedback information is passed as a {@link FeedbackDTO}, and the company
     * is identified by the company name.
     *
     * @param feedbackDTO the feedback data transfer object containing the feedback details
     * @param company the name of the company for which the feedback is being submitted
     * @return a {@link Mono} emitting the created {@link FeedbackDTO} after it has been saved, or an error if the operation fails
     */
    Mono<FeedbackDTO> createFeedbackToCompany(FeedbackDTO feedbackDTO, String company);

    /**
     * Retrieves feedback for a company filtered by rating and date range with pagination.
     *
     * <p>This method retrieves feedback entries for the specified company, where the feedback
     * rating is greater than or equal to the specified minimum rating. It also allows filtering
     * feedback by a date range and supports pagination with size and page parameters.
     *
     * @param company the name of the company whose feedback is being retrieved
     * @param minRating the minimum rating filter for the feedback
     * @param fromDate the start of the date range for filtering feedback (inclusive); can be null for no lower date limit
     * @param tillDate the end of the date range for filtering feedback (inclusive); can be null for no upper date limit
     * @param size the number of feedback entries to retrieve per page
     * @param page the page number to retrieve (used for pagination)
     * @return a {@link Flux} emitting the matching {@link FeedbackDTO} entries, or an empty {@link Flux} if no entries match
     */
    Flux<FeedbackDTO> getCompanyFeedbacksFromRatingOnwardsByDates(String company, Double minRating, LocalDateTime fromDate, LocalDateTime tillDate, int size, int page);
}
