package org.example.dal;

import org.example.data.LineStopEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LineStopRepository extends ReactiveCrudRepository<LineStopEntity, String> {
    @Query("SELECT * FROM line_stops LIMIT :size OFFSET :offset")
    Flux<LineStopEntity> findAllByPage(int offset, int size);

    @Query("SELECT * FROM line_stops WHERE line_number = :lineNumber AND stop_name = :stopName")
    Mono<LineStopEntity> findByLineNumberAndStopName(String lineNumber, String stopName);

    @Query("SELECT * FROM line_stops " +
            "WHERE line_number = :lineNumber " +
            "AND stop_order BETWEEN " +
            "(SELECT stop_order FROM line_stops WHERE line_number = :lineNumber AND stop_name = :originStopName) " +
            "AND " +
            "(SELECT stop_order FROM line_stops WHERE line_number = :lineNumber AND stop_name = :destinationStopName) " +
            "ORDER BY stop_order")
    Flux<LineStopEntity> findStopsBetweenOriginAndDestination(String lineNumber, String originStopName, String destinationStopName);
}
