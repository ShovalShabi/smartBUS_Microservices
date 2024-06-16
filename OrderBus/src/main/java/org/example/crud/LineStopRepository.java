package org.example.crud;

import org.example.data.LineStopEntity;
import org.example.data.StopId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineStopRepository extends ReactiveCrudRepository<LineStopEntity, StopId> {
}
