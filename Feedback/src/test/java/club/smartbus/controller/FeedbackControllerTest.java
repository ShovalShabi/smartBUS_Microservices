package club.smartbus.controller;

import club.smartbus.dal.FeedbackRepository;
import club.smartbus.dto.FeedbackDTO;
import club.smartbus.etc.TestSecurityConfig;
import club.smartbus.service.FeedbackService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

/**
 * Unit test class for the FeedbackController.
 * Tests the behavior of feedback-related APIs using WebFlux.
 */
@WebFluxTest(controllers = FeedbackController.class)
@Import(TestSecurityConfig.class)
public class FeedbackControllerTest {

    @MockBean
    private FeedbackService feedbackService;

    @MockBean
    private FeedbackRepository feedbackRepository;

    @MockBean
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    private WebTestClient webTestClient;

    /**
     * Sets up the test environment by initializing Mockito annotations.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Cleans up test resources after each test method execution by deleting feedback data for the "Egged" company.
     */
    @AfterEach
    void cleanUp() {
        webTestClient.delete()
                .uri("/Egged")
                .exchange()
                .expectStatus().isOk();
    }

    /**
     * Tests the creation of feedback for a company by making a POST request to the "/Egged" endpoint.
     * Verifies that the feedback creation logic is working and asserts that the returned feedback data matches the expectations.
     */
    @Test
    void testCreateFeedback() {
        FeedbackDTO feedbackDTO = new FeedbackDTO(5.0, "Egged", "123", "user@domain.com", "Great service", LocalDateTime.now());

        // Mock the feedback service to return the created feedback
        when(feedbackService.createFeedbackToCompany(any(FeedbackDTO.class), anyString())).thenReturn(Mono.just(feedbackDTO));

        // Make a POST request to create feedback and verify the response
        webTestClient.post()
                .uri("/Egged")
                .bodyValue(feedbackDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)
                .returnResult(FeedbackDTO.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNext(feedbackDTO)
                .verifyComplete();

        // Verify that the service method was called with the correct parameters
        verify(feedbackService).createFeedbackToCompany(any(FeedbackDTO.class), anyString());
    }

    /**
     * Tests retrieving feedback for a company with a minimum rating by making a GET request to the "/Egged" endpoint.
     * Verifies the response stream contains the expected feedback.
     */
    @Test
    void testGetCompanyFeedbacks() {
        FeedbackDTO feedbackDTO = new FeedbackDTO(4.0, "Egged", "123", "user@domain.com", "Good", LocalDateTime.now());

        // Mock the feedback service to return a Flux containing feedbackDTO
        when(feedbackService.getCompanyFeedbacksFromRatingOnwardsByDates(
                anyString(), anyDouble(), any(LocalDateTime.class), any(LocalDateTime.class), anyInt(), anyInt()))
                .thenReturn(Flux.just(feedbackDTO));

        // Verify the mock service output using StepVerifier
        StepVerifier.create(feedbackService.getCompanyFeedbacksFromRatingOnwardsByDates(
                        "Egged", 4.0, LocalDateTime.now().minusDays(1), LocalDateTime.now(), 0, 30))
                .expectNext(feedbackDTO)
                .verifyComplete();

        // Make a GET request to retrieve feedback and verify the response
        webTestClient.get()
                .uri("/Egged?minRating=4.0&size=30&page=0")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)
                .returnResult(FeedbackDTO.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(feedback -> {
                    assertNotNull(feedback, "Feedback should not be null");
                    assertEquals("Egged", feedback.getAgency(), "Agency name should match");
                    assertEquals(4.0, feedback.getRating(), "Rating should match");
                    assertEquals("123", feedback.getLineNumber(), "Line number should match");
                    assertEquals("user@domain.com", feedback.getUserEmail(), "User email should match");
                    assertEquals("Good", feedback.getAdditionalDetails(), "Additional details should match");
                })
                .expectComplete();

        // Verify that the service method was called with the correct parameters
        verify(feedbackService).getCompanyFeedbacksFromRatingOnwardsByDates(
                anyString(), anyDouble(), any(LocalDateTime.class), any(LocalDateTime.class), anyInt(), anyInt());
    }

    /**
     * Tests the deletion of all feedback for a company by making a DELETE request to the "/Egged" endpoint.
     * Verifies that the deletion logic works as expected.
     */
    @Test
    void testDeleteFeedbackForCompany() {
        // Mock the feedback service to return an empty Mono on deletion
        when(feedbackService.deleteAllFeedbackForCompany(anyString())).thenReturn(Mono.empty());

        // Make a DELETE request and verify the response
        webTestClient.delete()
                .uri("/Egged")
                .exchange()
                .expectStatus().isOk();

        // Verify that the service method was called with the correct parameters
        verify(feedbackService).deleteAllFeedbackForCompany(anyString());
    }
}
