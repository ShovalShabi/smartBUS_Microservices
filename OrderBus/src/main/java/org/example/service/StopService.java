package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.crud.LineStopRepository;
import org.example.data.LineStopEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class StopService {
    private final LineStopRepository lineStopRepository;

    public Flux<LineStopEntity> getAll() {
        return lineStopRepository.findAll();
    }
}
