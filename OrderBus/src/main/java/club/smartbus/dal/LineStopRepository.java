package club.smartbus.dal;

import club.smartbus.data.LineStopEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LineStopRepository extends ReactiveCrudRepository<LineStopEntity, String> {
    @Query("SELECT * FROM line_stops LIMIT :size OFFSET :offset")
    Flux<LineStopEntity> findAllByPage(int offset, int size);

    @Query("SELECT * FROM line_stops WHERE line_number = :lineNumber AND stop_name = :stopName LIMIT 1")
    Mono<LineStopEntity> findByLineNumberAndStopName(String lineNumber, String stopName);

    Flux<LineStopEntity> findLineStopEntitiesByLineNumber(@Param("line_number") String lineNumber);

    /**
     * Finds all bus line stops at a specific location by rounding the latitude and longitude values to 3 decimal places.
     *
     * <p>This query selects bus stops (line stops) where the rounded latitude and longitude values match
     * the provided coordinates. Rounding the values to 3 decimal places ensures that small variations in
     * coordinates do not prevent a match.</p>
     *
     * <p><strong>Query:</strong></p>
     * <pre>
     * SELECT * FROM line_stops WHERE ROUND(lat, 3) = :lat AND ROUND(lng, 3) = :lng
     * </pre>
     *
     * @param lat The latitude of the bus stop location, rounded to 3 decimal places in the query.
     * @param lng The longitude of the bus stop location, rounded to 3 decimal places in the query.
     * @return A Flux of LineStopEntity objects representing the bus stops at the specified rounded location.
     */
    @Query("SELECT * FROM line_stops WHERE ROUND(lat, 3) = :lat AND ROUND(lng, 3) = :lng")
    Flux<LineStopEntity> findByLineNumbersByStationLocation(double lat, double lng);

    @Query("SELECT stop_name FROM line_stops WHERE ROUND(lat, 3) = :lat AND ROUND(lng, 3) = :lng")
    Mono<String> findStationNameByLatitudeAndLongitude(double lat, double lng);
}
