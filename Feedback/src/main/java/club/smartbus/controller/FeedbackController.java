package club.smartbus.controller;

import club.smartbus.dto.FeedbackDTO;
import club.smartbus.service.FeedbackService;
import club.smartbus.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * The {@code FeedbackController} class handles HTTP requests related to feedback operations for companies.
 * It provides endpoints for creating new feedback and retrieving feedback based on several filter criteria.
 *
 * <p>This controller uses reactive programming with Spring WebFlux to return {@code Mono} and {@code Flux} responses.
 * Feedback is associated with a company, and feedback data can be created, fetched, or filtered by rating and date range.
 */
@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class FeedbackController {

    /**
     * The service that handles business logic for feedback operations.
     */
    @Autowired
    private final FeedbackService feedbackService;

    /**
     * Creates new feedback for the specified company.
     *
     * <p>This endpoint allows users to submit feedback for a specific company. The feedback is sent in the request body as a
     * {@link FeedbackDTO}, and the feedback will be processed and stored.
     *
     * @param company the name of the company to which the feedback is associated
     * @param feedbackDTO the feedback data transfer object containing the feedback details
     * @return a {@code Mono<FeedbackDTO>} containing the feedback data that was created
     */
    @PostMapping(path = "/{company}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<FeedbackDTO> createFeedback(
            @PathVariable String company,
            @RequestBody FeedbackDTO feedbackDTO) {

        return feedbackService.createFeedbackToCompany(feedbackDTO, company);
    }

    /**
     * Retrieves a list of feedbacks for a company, filtered by optional parameters such as rating and date range.
     *
     * <p>This endpoint allows users to retrieve feedbacks based on the company name, with optional filters
     * such as rating, date range, and pagination (size and page).
     *
     * @param company the name of the company for which to retrieve feedback
     * @param size the number of feedback records to retrieve per page (default is defined in {@link Constants#DEFAULT_PAGE_SIZE})
     * @param page the page number to retrieve (default is defined in {@link Constants#DEFAULT_PAGE})
     * @param minRating the minimum rating filter; only feedbacks with a rating greater than or equal to this will be returned (default is 0)
     * @param fromDate the start of the date range for filtering feedbacks; feedbacks created after this date will be included (optional)
     * @param tillDate the end of the date range for filtering feedbacks; feedbacks created before this date will be included (optional)
     * @return a {@code Flux<FeedbackDTO>} containing the feedbacks that match the provided filters
     */
    @GetMapping(
            path = {"/{company}"},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<FeedbackDTO> getCompanyFeedbacks(
            @PathVariable String company,
            @RequestParam(name = "size", required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", required = false, defaultValue = Constants.DEFAULT_PAGE) int page,
            @RequestParam(name = "minRating", required = false, defaultValue = "0") Double minRating,
            @RequestParam(name = "fromDate", required = false) //should receive a data as yyyy-MM-ddTHH:MM:SS
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fromDate,
            @RequestParam(name = "tillDate", required = false) //should receive a data as yyyy-MM-ddTHH:MM:SS
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime tillDate) {

        return feedbackService.getCompanyFeedbacksFromRatingOnwardsByDates(company,
                minRating,
                fromDate,
                tillDate,
                size,
                page);
    }


    /**
     * Deletes all feedbacks for the specified company.
     *
     * <p>This method is only active for development and testing purposes. It allows removing feedback entries associated
     * with a company, useful for cleaning up test data.
     *
     * @param company the name of the company whose feedback entries should be deleted
     * @return a {@link Mono<Void>} that completes when the deletion process is finished
     */
    @Profile({"dev", "test"})
    @DeleteMapping("/{company}")
    public Mono<Void> deleteAllFeedbackForCompany(@PathVariable String company) {
        return feedbackService.deleteAllFeedbackForCompany(company);
    }
}
