package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.data.LineStopEntity;
import org.example.service.StopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.example.utils.Constants.DEFAULT_PAGE;
import static org.example.utils.Constants.DEFAULT_PAGE_SIZE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/admin/stops")
@RequiredArgsConstructor
public class AdminController {
    private final StopService stopService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Flux<LineStopEntity> getAllStops(
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return stopService.getAll(size, page);
    }
}
