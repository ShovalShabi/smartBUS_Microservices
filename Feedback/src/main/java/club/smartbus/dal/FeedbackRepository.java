package club.smartbus.dal;

import club.smartbus.data.FeedbackEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends R2dbcRepository<FeedbackEntity, UUID> {

    // Query to fetch feedbacks for a company with optional date filtering and minimum rating
    @Query("SELECT * FROM feedback WHERE agency = :company AND rating >= :minRating " +
            "AND (:fromDate IS NULL OR creation_timestamp >= :fromDate) " +
            "AND (:tillDate IS NULL OR creation_timestamp <= :tillDate) " +
            "LIMIT :size OFFSET :page")
    Flux<FeedbackEntity> fetchFeedbacksByCompanyAndRatingAndDateRange(String company, Double minRating, LocalDateTime fromDate, LocalDateTime tillDate, int size, int page);
}
