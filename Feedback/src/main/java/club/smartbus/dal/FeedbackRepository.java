package club.smartbus.dal;

import club.smartbus.data.FeedbackEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The {@code FeedbackRepository} interface provides methods for interacting with the feedback table in the database.
 * This repository uses Spring Data R2DBC to facilitate reactive CRUD operations and custom query methods for the feedback data.
 *
 * <p>The repository allows fetching feedback records for a specific company based on optional filters such as
 * minimum rating and date range.
 */
@Repository
public interface FeedbackRepository extends R2dbcRepository<FeedbackEntity, UUID> {

    /**
     * Retrieves feedback records for a company based on the specified filters: minimum rating and date range.
     *
     * <p>This custom query filters the feedback records by company name, rating, and optionally, a date range
     * between {@code fromDate} and {@code tillDate}. If no date is provided, it retrieves all feedbacks
     * above the given rating. Results are paginated based on {@code size} and {@code page}.
     *
     * @param company the name of the company whose feedback records are being retrieved
     * @param minRating the minimum rating filter; only feedbacks with a rating greater than or equal to this will be returned
     * @param fromDate the start date for filtering feedback records; only feedbacks created after this date will be included (optional)
     * @param tillDate the end date for filtering feedback records; only feedbacks created before this date will be included (optional)
     * @param size the maximum number of feedback records to return (pagination)
     * @param page the page number to return (pagination)
     * @return a {@code Flux<FeedbackEntity>} representing the feedback records that match the filter criteria
     */
    @Query("SELECT * FROM feedback WHERE agency = :company AND rating >= :minRating " +
            "AND (:fromDate IS NULL OR creation_timestamp >= :fromDate) " +
            "AND (:tillDate IS NULL OR creation_timestamp <= :tillDate) " +
            "LIMIT :size OFFSET :page")
    Flux<FeedbackEntity> fetchFeedbacksByCompanyAndRatingAndDateRange(String company, Double minRating, LocalDateTime fromDate, LocalDateTime tillDate, int size, int page);
}
