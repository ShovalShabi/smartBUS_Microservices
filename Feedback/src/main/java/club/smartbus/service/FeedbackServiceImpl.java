package club.smartbus.service;

import club.smartbus.dal.FeedbackRepository;
import club.smartbus.data.FeedbackEntity;
import club.smartbus.dto.FeedbackDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementation of the {@link FeedbackService} interface, providing the business logic for feedback management.
 *
 * <p>This service interacts with the {@link FeedbackRepository} to manage feedback records for companies. It includes
 * methods to create feedback and retrieve feedback based on various criteria such as rating, date range, and pagination.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    /**
     * The repository that handles database operations for {@link FeedbackEntity}.
     */
    private final FeedbackRepository feedbackRepository;

    /**
     * Validator used to perform manual validation on the {@link FeedbackDTO}.
     */
    private final Validator validator;

    /**
     * Creates a new feedback entry for a specified company.
     *
     * <p>This method validates the input {@link FeedbackDTO} using the configured {@link Validator}. If validation
     * errors are found, an error will be returned, otherwise the feedback is saved to the database and returned.
     *
     * @param feedbackDTO the feedback data to be saved
     * @param company     the company to which the feedback belongs
     * @return a {@link Mono} emitting the saved {@link FeedbackDTO}, or an error if validation fails
     */
    @Override
    public Mono<FeedbackDTO> createFeedbackToCompany(@Valid FeedbackDTO feedbackDTO, String company) {
        // Manually validate the DTO using the Validator
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(feedbackDTO, "feedbackDTO");
        validator.validate(feedbackDTO, bindingResult);

        // If validation errors exist, return them as Mono.error
        if (bindingResult.hasErrors()) {
            log.error("Feedback doesn't composed properly");
            return Mono.error(new RuntimeException("Validation failed: " + bindingResult.getAllErrors()));
        }

        // Proceed to create the FeedbackEntity from the validated DTO
        FeedbackEntity feedbackEntity = new FeedbackEntity(feedbackDTO);

        // Ensure the ID is null to enforce creation (not updating an existing entity)
        feedbackEntity.setId(null);

        // Save to DB and immediately return the saved entity as FeedbackDTO
        return feedbackRepository.save(feedbackEntity)
                .map(FeedbackEntity::toDTO)
                .doOnSuccess(savedFeedback -> log.info("Feedback saved successfully for company: {}", company));
    }

    /**
     * Retrieves feedback entries for a company based on the specified rating and date range, with pagination support.
     *
     * <p>The method checks that the start date is not after the end date, and retrieves feedbacks that match the criteria
     * of minimum rating, date range, company, and pagination settings.
     *
     * @param company   the company to retrieve feedback for
     * @param minRating the minimum rating for feedback entries to be included
     * @param fromDate  the start of the date range to filter feedback (inclusive); can be null for no lower limit
     * @param tillDate  the end of the date range to filter feedback (inclusive); can be null for no upper limit
     * @param size      the number of feedback entries per page
     * @param page      the page number for pagination
     * @return a {@link Flux} emitting matching {@link FeedbackDTO} entries or an error if date validation fails
     */
    @Override
    public Flux<FeedbackDTO> getCompanyFeedbacksFromRatingOnwardsByDates(String company, Double minRating, LocalDateTime fromDate, LocalDateTime tillDate, int size, int page) {
        // Validate that fromDate is not after tillDate
        if (fromDate != null && tillDate != null && fromDate.isAfter(tillDate)) {
            log.error("Received 'fromDate' after 'tillDate'");
            return Flux.error(new IllegalArgumentException("'fromDate' cannot be after 'tillDate'"));
        }

        // Call the repository method with the provided parameters
        return feedbackRepository.fetchFeedbacksByCompanyAndRatingAndDateRange(company, minRating, fromDate, tillDate, size, page)
                .map(FeedbackEntity::toDTO)  // Convert each FeedbackEntity to FeedbackDTO
                .switchIfEmpty(Flux.empty()) // Return empty Flux if no results
                .doOnNext(feedback -> log.info("Retrieved feedback: {}", feedback))
                .doOnError(error -> log.error("Error retrieving feedbacks", error));
    }

    /**
     * Deletes all feedback entries associated with a specific company.
     *
     * <p>This method deletes all feedback records from the database that are associated with the provided company name.
     * It is typically used in development and testing environments to remove test data or reset feedback records for a company.
     *
     * <p>The method interacts with the {@link FeedbackRepository} to perform the deletion, logging the operation upon success.
     * After the deletion is complete, a {@code Mono<Void>} is returned, which completes successfully once the operation is done.
     *
     * @param company the name of the company whose feedback records should be deleted
     * @return a {@link Mono<Void>} that completes once the deletion operation is finished
     */
    @Override
    public Mono<Void> deleteAllFeedbackForCompany(String company) {
        return feedbackRepository.deleteByCompany(company)
                .doOnSuccess(unused -> log.info("Deleted all feedbacks for company: {}", company))
                .then();
    }
}
