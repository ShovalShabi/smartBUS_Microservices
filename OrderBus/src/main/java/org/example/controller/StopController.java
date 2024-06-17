package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.data.LineStopEntity;
import org.example.service.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.example.utils.Constants.DEFAULT_PAGE;
import static org.example.utils.Constants.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping(path = "/stops")
public class StopController {
    private StopService stopService;

    @Autowired
    public StopController(StopService stopService) {
        this.stopService = stopService;
    }

    @GetMapping(produces = "application/stream+json")
    public Flux<LineStopEntity> getAllStops(
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return stopService.getAll(size, page);
    }
}
