package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.data.LineStopEntity;
import org.example.dto.transit.Station;
import org.example.service.BusService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.example.utils.Constants.DEFAULT_PAGE;
import static org.example.utils.Constants.DEFAULT_PAGE_SIZE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/bus")
@RequiredArgsConstructor
public class BusController {
    private final BusService busService;

    @GetMapping(
            path = {"/stationsByLine/{lineNumber}"},
            produces = APPLICATION_JSON_VALUE)
    public Flux<Station> getAllStops(
            @PathVariable String lineNumber,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(name = "startStation", required = false, defaultValue = "") String startStation,
            @RequestParam(name = "stopStation", required = false, defaultValue = "") String stopStation) {
        return busService.getBusLineStations(lineNumber, startStation, stopStation, size, page);
    }
}
