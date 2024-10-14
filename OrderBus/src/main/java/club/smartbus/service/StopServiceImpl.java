package club.smartbus.service;

import club.smartbus.boundaries.stops.StopsRequest;
import club.smartbus.boundaries.stops.StopsResponse;
import club.smartbus.dal.LineStopRepository;
import club.smartbus.data.LineStopEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link StopService} interface responsible for managing bus stop data.
 * This service interacts with a repository to retrieve bus stops and integrates with the {@link RoutesService}
 * to fetch routes and their intermediate stations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StopServiceImpl implements StopService {

    private final LineStopRepository lineStopCrud;
    private final RoutesService routesService;

    /**
     * Retrieves bus stops along the routes with intermediate stations.
     *
     * <p>This method uses the {@link RoutesService#getRoutesWithIntermediateStations(StopsRequest)} to fetch
     * route details with intermediate stops based on the provided {@link StopsRequest}. The resulting route details
     * are mapped to {@link StopsResponse} objects.</p>
     *
     * @param stopsRequest the request containing the route information for which stops need to be retrieved.
     * @param size         the number of stops to return per page.
     * @param page         the page number to retrieve.
     * @return a {@link Flux<StopsResponse>} containing the stop details for the requested route.
     */
    @Override
    public Flux<StopsResponse> getAll(StopsRequest stopsRequest, int size, int page) {
        return routesService.getRoutesWithIntermediateStations(stopsRequest)
                .flatMap(routeResponse -> Mono.just(new StopsResponse(routeResponse)));
    }

    /*  ADMIN COMMANDS  */

    /**
     * Retrieves a paginated list of all bus stops.
     *
     * <p>This method fetches bus stop data from the repository with pagination based on the provided
     * {@code size} and {@code page} parameters. The method logs the retrieval process and handles any errors
     * that may occur during the fetch.</p>
     *
     * @param size the number of stops to return per page.
     * @param page the page number to retrieve.
     * @return a {@link Flux<LineStopEntity>} containing the paginated list of all bus stops.
     */
    @Override
    public Flux<LineStopEntity> getAll(int size, int page) {
        try {
            log.info("Getting all stops");
            int offset = page * size;
            return lineStopCrud.findAllByPage(offset, size);
        } catch (Exception e) {
            log.error("Error getting all stops", e);
            return Flux.error(e);
        }
    }
}
