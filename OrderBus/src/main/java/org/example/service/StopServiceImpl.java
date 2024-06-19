package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.boundaries.stops.StopsRequest;
import org.example.boundaries.stops.StopsResponse;
import org.example.dal.LineStopRepository;
import org.example.data.LineStopEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class StopServiceImpl implements StopService {
    private final LineStopRepository lineStopCrud;
    private final RoutesService routesService;

    @Override
    public Flux<StopsResponse> getAll(StopsRequest stopsRequest, int size, int page) {
        return routesService.getRoutesWithIntermediateStations(stopsRequest)
                .flatMap(routeResponse -> Mono.just(new StopsResponse(routeResponse)));
    }

    /*  ADMIN COMMANDS  */

    @Override
    public Flux<LineStopEntity> getAll(int size, int page) {
        try {
            log.info("Getting all stops");
            int offset = page * size;
            return lineStopCrud.findAllByPage(offset, size);
        } catch (Exception e) {
            log.error("Error getting all stops", e);
            return Flux.error(e);
        }
    }
}
