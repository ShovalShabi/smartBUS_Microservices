package org.example.dal;

import org.example.data.LineStopEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LineStopRepository extends ReactiveCrudRepository<LineStopEntity, String> {
    @Query("SELECT * FROM line_stops LIMIT :size OFFSET :offset")
    Flux<LineStopEntity> findAllByPage(int offset, int size);

    @Query("SELECT * FROM line_stops WHERE line_number = :lineNumber AND stop_name = :stopName LIMIT 1")
    Mono<LineStopEntity> findByLineNumberAndStopName(String lineNumber, String stopName);

    @Query("SELECT ls.* FROM line_stops ls " +
            "WHERE ls.line_number = :lineNumber " +
            "AND ls.stop_order BETWEEN " +
            "(SELECT ls1.stop_order FROM line_stops ls1 WHERE ls1.line_number = :lineNumber AND ls1.stop_name = :originStopName LIMIT 1) " +
            "AND " +
            "(SELECT ls2.stop_order FROM line_stops ls2 WHERE ls2.line_number = :lineNumber AND ls2.stop_name = :destinationStopName LIMIT 1) " +
            "ORDER BY ls.stop_order")
    Flux<LineStopEntity> findStopsBetweenOriginAndDestination(String lineNumber, String originStopName, String destinationStopName);
}
