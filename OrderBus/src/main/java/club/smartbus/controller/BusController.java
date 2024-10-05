package club.smartbus.controller;

import club.smartbus.boundaries.stations.StationsRequest;
import club.smartbus.boundaries.stops.StopsRequest;
import club.smartbus.dto.transit.Station;
import club.smartbus.service.BusService;
import club.smartbus.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/bus")
@RequiredArgsConstructor
public class BusController {

    @Autowired
    private final BusService busService;

    @GetMapping(
            path = {"/stations"},
            produces = APPLICATION_JSON_VALUE)
    public Flux<Station> getAllStops(
            @RequestBody StationsRequest stationsRequest,
            @RequestParam(name = "size", required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", required = false, defaultValue = Constants.DEFAULT_PAGE) int page,
            @RequestParam(name = "startStation", required = false, defaultValue = "") String startStation,
            @RequestParam(name = "stopStation", required = false, defaultValue = "") String stopStation) {
        return busService.getBusLineStations(stationsRequest, startStation, stopStation, size, page);
    }
}
