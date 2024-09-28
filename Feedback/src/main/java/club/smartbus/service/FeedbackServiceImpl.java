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

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final Validator validator;  // Validator for input validation

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
        System.err.println(feedbackEntity);

        // Save to DB and immediately return the saved entity as FeedbackDTO
        return feedbackRepository.save(feedbackEntity)
                .map(FeedbackEntity::toDTO)
                .doOnSuccess(savedFeedback -> log.info("Feedback saved successfully for company: {}", company));
    }

    @Override
    public Flux<FeedbackDTO> getCompanyFeedbacksFromRatingOnwardsByDates(String company, Double minRating, LocalDateTime fromDate, LocalDateTime tillDate, int size, int page) {

        System.err.println("I'm in service");
        // Validate that fromDate is not after tillDate
        if (fromDate != null && tillDate != null && fromDate.isAfter(tillDate)) {
            log.error("Received 'fromDate' after 'tillDate'");
            return Flux.error(new IllegalArgumentException("'fromDate' cannot be after 'tillDate'"));
        }
        System.err.println("passed validation");

        // Call the repository method with the provided parameters
        return feedbackRepository.fetchFeedbacksByCompanyAndRatingAndDateRange(company, minRating, fromDate, tillDate, size, page)
                .map(FeedbackEntity::toDTO)  // Convert each FeedbackEntity to FeedbackDTO
                .doOnNext(feedback -> log.info("Retrieved feedback: {}", feedback))
                .doOnError(error -> log.error("Error retrieving feedbacks", error));
    }
}
