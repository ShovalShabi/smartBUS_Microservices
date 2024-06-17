package org.example.service;

import org.example.data.LineStopEntity;
import reactor.core.publisher.Flux;

public interface StopService {
    Flux<LineStopEntity> getAll(int size, int page);
}
