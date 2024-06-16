package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.data.LineStopEntity;
import org.example.service.StopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/stops")
@RequiredArgsConstructor
public class StopController {
    private final StopService stopService;

    @GetMapping
    public Flux<LineStopEntity> getAllStops() {
        return stopService.getAll();
    }
}
