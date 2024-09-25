package club.smartbus.service;

import club.smartbus.boundaries.stops.StopsRequest;
import club.smartbus.boundaries.stops.StopsResponse;
import club.smartbus.data.LineStopEntity;
import reactor.core.publisher.Flux;

public interface StopService {
    Flux<StopsResponse> getAll(StopsRequest stopsRequest, int size, int page);

    Flux<LineStopEntity> getAll(int size, int page);
}
