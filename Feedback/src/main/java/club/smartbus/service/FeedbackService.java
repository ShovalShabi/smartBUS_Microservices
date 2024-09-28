package club.smartbus.service;


import club.smartbus.dto.FeedbackDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface FeedbackService {

    Mono<FeedbackDTO> createFeedbackToCompany(FeedbackDTO feedbackDTO, String company);

    Flux<FeedbackDTO> getCompanyFeedbacksFromRatingOnwardsByDates(String company, Double minRating, LocalDateTime fromDate, LocalDateTime tillDate, int size, int page);
}