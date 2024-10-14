package club.smartbus.dal;

import club.smartbus.data.LineStopEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository interface for performing reactive CRUD operations on {@link LineStopEntity} entities.
 * This interface extends {@link ReactiveCrudRepository} and provides custom query methods for bus line stop data.
 */
public interface LineStopRepository extends ReactiveCrudRepository<LineStopEntity, String> {

    /**
     * Retrieves a paginated list of bus line stops from the database.
     *
     * @param offset The offset value for pagination (number of rows to skip).
     * @param size   The number of records to retrieve.
     * @return A Flux of {@link LineStopEntity} objects representing the bus line stops.
     */
    @Query("SELECT * FROM line_stops LIMIT :size OFFSET :offset")
    Flux<LineStopEntity> findAllByPage(int offset, int size);

    /**
     * Retrieves a specific bus line stop entity based on the line number and stop name.
     *
     * @param lineNumber The line number associated with the bus stop.
     * @param stopName   The name of the stop.
     * @return A Mono of {@link LineStopEntity} representing the bus stop, or an empty Mono if no match is found.
     */
    @Query("SELECT * FROM line_stops WHERE line_number = :lineNumber AND stop_name = :stopName LIMIT 1")
    Mono<LineStopEntity> findByLineNumberAndStopName(String lineNumber, String stopName);

    /**
     * Retrieves all bus stops for a specific bus line.
     *
     * @param lineNumber The line number of the bus route.
     * @return A Flux of {@link LineStopEntity} objects representing the stops along the specified bus line.
     */
    Flux<LineStopEntity> findLineStopEntitiesByLineNumber(@Param("line_number") String lineNumber);

    /**
     * Finds all bus line stops at a specific location by rounding the latitude and longitude values to 3 decimal places.
     *
     * <p>This query selects bus stops where the rounded latitude and longitude values match the provided coordinates.
     * Rounding to 3 decimal places ensures that minor variations in coordinates do not prevent a match.</p>
     *
     * <p><strong>Query:</strong></p>
     * <pre>
     * SELECT * FROM line_stops WHERE ROUND(lat, 3) = :lat AND ROUND(lng, 3) = :lng
     * </pre>
     *
     * @param lat The latitude of the bus stop location, rounded to 3 decimal places.
     * @param lng The longitude of the bus stop location, rounded to 3 decimal places.
     * @return A Flux of {@link LineStopEntity} objects representing the bus stops at the specified rounded location.
     */
    @Query("SELECT * FROM line_stops WHERE ROUND(lat, 3) = :lat AND ROUND(lng, 3) = :lng")
    Flux<LineStopEntity> findByLineNumbersByStationLocation(double lat, double lng);

    /**
     * Retrieves the name of a bus stop based on its rounded latitude and longitude.
     *
     * <p>This query selects the stop name where the rounded latitude and longitude values match the provided coordinates.
     * Rounding to 3 decimal places ensures that minor variations in coordinates do not prevent a match.</p>
     *
     * <p><strong>Query:</strong></p>
     * <pre>
     * SELECT stop_name FROM line_stops WHERE ROUND(lat, 3) = :lat AND ROUND(lng, 3) = :lng
     * </pre>
     *
     * @param lat The latitude of the bus stop, rounded to 3 decimal places.
     * @param lng The longitude of the bus stop, rounded to 3 decimal places.
     * @return A Mono of String representing the stop name at the specified location, or an empty Mono if no match is found.
     */
    @Query("SELECT stop_name FROM line_stops WHERE ROUND(lat, 3) = :lat AND ROUND(lng, 3) = :lng")
    Mono<String> findStationNameByLatitudeAndLongitude(double lat, double lng);
}
