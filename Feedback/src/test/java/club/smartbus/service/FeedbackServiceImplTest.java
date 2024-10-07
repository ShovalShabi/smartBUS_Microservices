package club.smartbus.service;

import club.smartbus.dal.FeedbackRepository;
import club.smartbus.data.FeedbackEntity;
import club.smartbus.dto.FeedbackDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link FeedbackServiceImpl}.
 * This class tests various feedback-related functionalities like creating, retrieving, and deleting feedback.
 */
class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private AutoCloseable closeable;

    /**
     * Initializes the mocks before each test method execution.
     */
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Closes the resources after each test method execution.
     *
     * @throws Exception if an exception occurs while closing resources.
     */
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Tests the successful creation of feedback for a company.
     * Verifies the feedback creation process, ensuring that the service saves the feedback correctly.
     */
    @Test
    void testCreateFeedbackToCompany_Success() {
        // Arrange
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // Truncate to seconds to avoid precision issues
        FeedbackDTO feedbackDTO = new FeedbackDTO(5.0, "Egged", "123", "user@domain.com", "Great service", now);
        FeedbackEntity feedbackEntity = new FeedbackEntity(feedbackDTO);
        feedbackEntity.setId(UUID.randomUUID());

        doNothing().when(validator).validate(eq(feedbackDTO), any(BeanPropertyBindingResult.class));
        when(feedbackRepository.save(any(FeedbackEntity.class))).thenReturn(Mono.just(feedbackEntity));

        // Act
        Mono<FeedbackDTO> result = feedbackService.createFeedbackToCompany(feedbackDTO, "Egged");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(savedFeedbackDTO ->
                        savedFeedbackDTO.getRating().equals(feedbackDTO.getRating()) &&
                                savedFeedbackDTO.getAgency().equals(feedbackDTO.getAgency()) &&
                                savedFeedbackDTO.getLineNumber().equals(feedbackDTO.getLineNumber()) &&
                                savedFeedbackDTO.getUserEmail().equals(feedbackDTO.getUserEmail()) &&
                                savedFeedbackDTO.getAdditionalDetails().equals(feedbackDTO.getAdditionalDetails()) &&
                                savedFeedbackDTO.getCreationTimestamp().truncatedTo(ChronoUnit.SECONDS).equals(feedbackDTO.getCreationTimestamp()) // Truncate timestamp to seconds for comparison
                )
                .verifyComplete();

        verify(validator).validate(eq(feedbackDTO), any(BeanPropertyBindingResult.class));
        verify(feedbackRepository).save(any(FeedbackEntity.class));
    }


    /**
     * Tests the failure of feedback creation due to validation errors.
     * Simulates a validation failure and verifies that the feedback is not saved.
     */
    @Test
    void testCreateFeedbackToCompany_ValidationFailure() {
        // Arrange
        FeedbackDTO feedbackDTO = new FeedbackDTO(null, "Egged", "123", "invalid-email", "", LocalDateTime.now());

        // Mock the validation process to simulate validation failure
        doAnswer(invocation -> {
            BeanPropertyBindingResult bindingResult = invocation.getArgument(1);
            bindingResult.reject("validation failed");
            return null;
        }).when(validator).validate(eq(feedbackDTO), any(BeanPropertyBindingResult.class));

        // Act
        Mono<FeedbackDTO> result = feedbackService.createFeedbackToCompany(feedbackDTO, "Egged");

        // Assert
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(validator).validate(eq(feedbackDTO), any(BeanPropertyBindingResult.class));
        verify(feedbackRepository, never()).save(any(FeedbackEntity.class));
    }

    /**
     * Tests the successful retrieval of feedback for a company with a given rating.
     * Verifies that feedback is retrieved correctly from the repository.
     */
    @Test
    void testGetCompanyFeedbacks_Success() {
        // Arrange
        FeedbackEntity feedbackEntity = new FeedbackEntity(UUID.randomUUID(), 4.0, "Egged", "123", "user@domain.com", "Good", LocalDateTime.now());
        when(feedbackRepository.fetchFeedbacksByCompanyAndRatingAndDateRange(
                eq("Egged"),
                eq(4.0),
                isNull(),
                isNull(),
                eq(30),
                eq(0))
        ).thenReturn(Flux.just(feedbackEntity)); // Return a non-null Flux

        // Act
        Flux<FeedbackDTO> result = feedbackService.getCompanyFeedbacksFromRatingOnwardsByDates("Egged", 4.0, null, null, 30, 0);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getAgency().equals("Egged"))
                .verifyComplete();

        verify(feedbackRepository).fetchFeedbacksByCompanyAndRatingAndDateRange(
                eq("Egged"),
                eq(4.0),
                isNull(),
                isNull(),
                eq(30),
                eq(0)
        );
    }

    /**
     * Tests the scenario where no feedback is found for the company.
     * Ensures that an empty result is returned when no feedback is found.
     */
    @Test
    void testGetCompanyFeedbacks_EmptyResult() {
        // Arrange: Ensure that the repository returns an empty Flux when no feedback is found
        when(feedbackRepository.fetchFeedbacksByCompanyAndRatingAndDateRange(
                eq("Egged"),
                eq(4.0),
                isNull(),
                isNull(),
                eq(30),
                eq(0))
        ).thenReturn(Flux.empty());

        // Act: Call the service method
        Flux<FeedbackDTO> result = feedbackService.getCompanyFeedbacksFromRatingOnwardsByDates("Egged", 4.0, null, null, 30, 0);

        // Assert: Verify that the Flux is empty
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        // Verify the repository interaction
        verify(feedbackRepository).fetchFeedbacksByCompanyAndRatingAndDateRange(
                eq("Egged"),
                eq(4.0),
                isNull(),
                isNull(),
                eq(30),
                eq(0)
        );
    }

    /**
     * Tests the scenario where an invalid date range is provided for retrieving feedback.
     * Ensures that an error is thrown when the date range is invalid.
     */
    @Test
    void testGetCompanyFeedbacks_InvalidDateRange() {
        // Arrange
        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime tillDate = fromDate.minusDays(1);

        // Act
        Flux<FeedbackDTO> result = feedbackService.getCompanyFeedbacksFromRatingOnwardsByDates("Egged", 4.0, fromDate, tillDate, 30, 0);

        // Assert
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(feedbackRepository, never()).fetchFeedbacksByCompanyAndRatingAndDateRange(anyString(), anyDouble(), any(LocalDateTime.class), any(LocalDateTime.class), anyInt(), anyInt());
    }

    /**
     * Tests the successful deletion of all feedback for a company.
     * Verifies that the feedback repository successfully deletes the feedback.
     */
    @Test
    void testDeleteAllFeedbackForCompany_Success() {
        // Arrange
        when(feedbackRepository.deleteByCompany(anyString())).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = feedbackService.deleteAllFeedbackForCompany("Egged");

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(feedbackRepository).deleteByCompany(anyString());
    }
}
