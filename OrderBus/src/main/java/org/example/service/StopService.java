package org.example.service;

import org.example.boundaries.stops.StopsRequest;
import org.example.boundaries.stops.StopsResponse;
import org.example.data.LineStopEntity;
import reactor.core.publisher.Flux;

public interface StopService {
    Flux<StopsResponse> getAll(StopsRequest stopsRequest, int size, int page);
    Flux<LineStopEntity> getAll(int size, int page);
}
