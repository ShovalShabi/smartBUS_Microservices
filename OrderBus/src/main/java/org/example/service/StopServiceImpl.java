package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dal.LineStopRepository;
import org.example.data.LineStopEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class StopServiceImpl implements StopService {
    private final LineStopRepository lineStopCrud;

    @Override
    public Flux<LineStopEntity> getAll(int size, int page) {
        try {
            log.info("Getting all stops");
            PageRequest pageReq = PageRequest.of(page, size);
            return Flux.fromIterable(lineStopCrud.findAll(pageReq));
        } catch (Exception e) {
            log.error("Error getting all stops", e);
            return Flux.error(e);
        }
    }
}
