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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void cleanUp() {
        webTestClient.delete()
                .uri("/Egged")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testCreateFeedback() {
        FeedbackDTO feedbackDTO = new FeedbackDTO(5.0, "Egged", "123", "user@domain.com", "Great service", LocalDateTime.now());
        when(feedbackService.createFeedbackToCompany(any(FeedbackDTO.class), anyString())).thenReturn(Mono.just(feedbackDTO));

        webTestClient.post()
                .uri("/Egged")
                .bodyValue(feedbackDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)  // Allow charset in the response
                .returnResult(FeedbackDTO.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNext(feedbackDTO)  // Expect one feedback DTO from the event stream
                .verifyComplete();  // Complete the verification

        verify(feedbackService).createFeedbackToCompany(any(FeedbackDTO.class), anyString());
    }


    @Test
    void testGetCompanyFeedbacks() {
        FeedbackDTO feedbackDTO = new FeedbackDTO(4.0, "Egged", "123", "user@domain.com", "Good", LocalDateTime.now());

        // Mock the service to return a Flux with feedbackDTO
        when(feedbackService.getCompanyFeedbacksFromRatingOnwardsByDates(
                anyString(), anyDouble(), any(LocalDateTime.class), any(LocalDateTime.class), anyInt(), anyInt()))
                .thenReturn(Flux.just(feedbackDTO));

        // Directly verify the mock output with StepVerifier before calling the webTestClient
        StepVerifier.create(feedbackService.getCompanyFeedbacksFromRatingOnwardsByDates(
                        "Egged", 4.0, LocalDateTime.now().minusDays(1), LocalDateTime.now(), 0, 30))
                .expectNext(feedbackDTO)
                .verifyComplete();

        // Use the webTestClient to send a GET request
        webTestClient.get()
                .uri("/Egged?minRating=4.0&size=30&page=0")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
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

        // Verify that the mocked service method was called with the expected arguments
        verify(feedbackService).getCompanyFeedbacksFromRatingOnwardsByDates(
                anyString(), anyDouble(), any(LocalDateTime.class), any(LocalDateTime.class), anyInt(), anyInt());
    }


    @Test
    void testDeleteFeedbackForCompany() {
        when(feedbackService.deleteAllFeedbackForCompany(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/Egged")
                .exchange()
                .expectStatus().isOk();

        verify(feedbackService).deleteAllFeedbackForCompany(anyString());
    }
}
