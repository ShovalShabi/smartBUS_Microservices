package org.example.dal;

import org.example.data.LineStopEntity;
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
}
