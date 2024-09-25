package club.smartbus.controller;

import club.smartbus.boundaries.stops.StopsRequest;
import club.smartbus.boundaries.stops.StopsResponse;
import club.smartbus.service.StopService;
import club.smartbus.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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
            @RequestParam(name = "size", required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", required = false, defaultValue = Constants.DEFAULT_PAGE) int page) {
        return stopService.getAll(stopsRequest, size, page);
    }
}
