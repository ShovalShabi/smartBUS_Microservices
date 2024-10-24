package club.smartbus.controller;

import club.smartbus.dto.stops.StopsRequestDTO;
import club.smartbus.dto.stops.StopsResponseDTO;
import club.smartbus.service.stop.StopService;
import club.smartbus.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller responsible for handling bus stop-related operations.
 * This controller provides endpoints for retrieving stops information based on a request.
 * It utilizes a reactive, non-blocking approach using {@link Flux} to return a stream of {@link StopsResponseDTO} objects.
 */
@RestController
@RequestMapping(path = "/stops")
@RequiredArgsConstructor
public class StopController {

    /**
     * Service layer responsible for handling bus stop-related business logic.
     * The {@link StopService} is injected to manage operations like fetching stops information.
     */
    @Autowired
    private final StopService stopService;

    /**
     * Retrieves all bus stops based on the provided {@link StopsRequestDTO}.
     * The results can be paginated by specifying the page number and size.
     *
     * @param stopsRequestDTO The request object containing details such as whether to order a bus and the associated route.
     * @param size         Optional parameter to specify the number of stops per page (default: {@link Constants#DEFAULT_PAGE_SIZE}).
     * @param page         Optional parameter to specify the page number to retrieve (default: {@link Constants#DEFAULT_PAGE}).
     * @return A reactive stream (Flux) of {@link StopsResponseDTO} objects representing the bus stops.
     */
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public Flux<StopsResponseDTO> getAllStops(
            @RequestBody StopsRequestDTO stopsRequestDTO,
            @RequestParam(name = "size", required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", required = false, defaultValue = Constants.DEFAULT_PAGE) int page) {
        return stopService.getAll(stopsRequestDTO, size, page);
    }
}
