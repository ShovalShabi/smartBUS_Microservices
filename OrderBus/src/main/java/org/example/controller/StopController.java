package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.boundaries.stops.StopsRequest;
import org.example.boundaries.stops.StopsResponse;
import org.example.service.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.example.utils.Constants.DEFAULT_PAGE;
import static org.example.utils.Constants.DEFAULT_PAGE_SIZE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/stops")
@RequiredArgsConstructor
public class StopController {

    @Autowired
    private final StopService stopService;

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public Flux<StopsResponse> getAllStops(
            @RequestBody StopsRequest stopsRequest,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page) {
        return stopService.getAll(stopsRequest, size, page);
    }
}
