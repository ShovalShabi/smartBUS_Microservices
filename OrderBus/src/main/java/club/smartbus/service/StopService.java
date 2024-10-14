package club.smartbus.service;

import club.smartbus.dto.stops.StopsRequestDTO;
import club.smartbus.dto.stops.StopsResponseDTO;
import club.smartbus.data.LineStopEntity;
import reactor.core.publisher.Flux;

/**
 * Service interface for managing bus stops.
 * This service provides methods for retrieving bus stop information, either by specific request or with pagination.
 */
public interface StopService {

    /**
     * Retrieves a paginated list of bus stops based on the provided request details.
     *
     * <p>This method processes the {@link StopsRequestDTO} and returns the corresponding stops. The request
     * may include parameters like the bus line number, company, or stop filters to return the relevant data.</p>
     *
     * <p>The results are paginated based on the {@code size} and {@code page} parameters.</p>
     *
     * @param stopsRequestDTO the request containing filters for the bus stops.
     * @param size         the number of stops to return per page.
     * @param page         the page number to retrieve.
     * @return a {@link Flux< StopsResponseDTO >} containing the filtered and paginated list of bus stops.
     */
    Flux<StopsResponseDTO> getAll(StopsRequestDTO stopsRequestDTO, int size, int page);

    /**
     * Retrieves a paginated list of all bus stops, without any specific request filters.
     *
     * <p>This method returns all bus stops from the data source with pagination applied. The stops are retrieved
     * based on the size of the page and the page number provided.</p>
     *
     * @param size the number of stops to return per page.
     * @param page the page number to retrieve.
     * @return a {@link Flux<LineStopEntity>} containing the paginated list of bus stops.
     */
    Flux<LineStopEntity> getAll(int size, int page);
}
