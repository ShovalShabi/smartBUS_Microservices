package club.smartbus.controller;

import club.smartbus.dto.stations.StationsRequest;
import club.smartbus.dto.transit.Station;
import club.smartbus.service.BusService;
import club.smartbus.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller responsible for handling bus-related operations, such as retrieving bus stations.
 * This controller exposes endpoints for interacting with bus station data and utilizes a non-blocking, reactive approach
 * with the help of {@link Flux} to return streams of bus stations.
 */
@RestController
@RequestMapping(path = "/bus")
@RequiredArgsConstructor
public class BusController {

    /**
     * Service layer dependency for handling bus-related operations.
     * The {@link BusService} is responsible for the core business logic.
     */
    @Autowired
    private final BusService busService;

    /**
     * Retrieves all bus stops for a given bus line based on the provided {@link StationsRequest}.
     * The results are paginated, and the user can specify a start station and a stop station for filtering.
     *
     * @param stationsRequest  Request object containing details like bus line number and agency.
     * @param size             Optional parameter to specify the number of results per page (default: {@link Constants#DEFAULT_PAGE_SIZE}).
     * @param page             Optional parameter to specify the page number to retrieve (default: {@link Constants#DEFAULT_PAGE}).
     * @param startStation     Optional parameter to filter results by the starting station.
     * @param stopStation      Optional parameter to filter results by the stopping station.
     * @return A reactive stream (Flux) of {@link Station} objects representing the bus stops.
     */
    @PostMapping(
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
