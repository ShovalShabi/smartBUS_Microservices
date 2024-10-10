package club.smartbus.service;

import club.smartbus.boundaries.stations.StationsRequest;
import club.smartbus.dal.LineStopRepository;
import club.smartbus.data.LineStopEntity;
import club.smartbus.dto.transit.Station;
import club.smartbus.utils.ConvertEntityToDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {
    private final LineStopRepository lineStopRepository;

    /**
     * Retrieves a paginated list of stations for a given bus line between the specified start and stop stations.
     *
     * <p>This method handles three primary scenarios:</p>
     * <ul>
     *   <li><strong>Scenario 1:</strong> If {@code startStation} is provided and {@code stopStation} is empty,
     *       the method returns all stations from the specified {@code startStation} to the end of the list.</li>
     *   <li><strong>Scenario 2:</strong> If {@code startStation} is empty and {@code stopStation} is provided,
     *       the method returns all stations from the beginning of the list to the specified {@code stopStation}.</li>
     *   <li><strong>Scenario 3:</strong> If both {@code startStation} and {@code stopStation} are provided,
     *       the method returns all stations between the two specified stations.</li>
     * </ul>
     *
     * <p>If no stations are found for the provided {@code lineNumber}, a {@link RuntimeException} is returned as a
     * {@link Flux#error(Throwable)}. The method also handles cases where the start or stop station is not found by
     * returning an appropriate error message.</p>
     *
     * <p>The method applies pagination based on the {@code size} and {@code page} parameters.</p>
     *
     * @param stationsRequest   the line number and agency name of the bus route
     * @param startStation      the name of the start station (can be empty)
     * @param stopStation       the name of the stop station (can be empty)
     * @param size              the number of stations to return per page
     * @param page              the page number to retrieve
     * @return a {@link Flux<Station>} containing the paginated list of stations between the start and stop stations
     * @throws RuntimeException         if no stations are found for the provided line number or if the start/stop station is not found
     * @throws IllegalArgumentException if the start station is after the stop station
     */
    @Override
    public Flux<Station> getBusLineStations(StationsRequest stationsRequest, String startStation, String stopStation, int size, int page) {
        // Fetch all stations for the line number
        return lineStopRepository.findLineStopEntitiesByLineNumber(stationsRequest.getLineNumber())
                .collectList()
                .flatMapMany(stopsList -> {
                    if (stopsList.isEmpty()) {
                        // If no stops are found, return a Flux error
                        return Flux.error(new RuntimeException("No stations found for line number '" + stationsRequest.getLineNumber() + "'"));
                    }

                    // Filter for stops of the requested agency
                    stopsList = stopsList.stream()
                            .filter(station -> station.getAgency_name().equals(stationsRequest.getAgency()))
                            .toList();

                    // Find the requested direction vector of stations

                    int startIndex = 0;
                    int endIndex = stopsList.size();

                    // Determine start index if startStation is provided
                    if (startStation != null && !startStation.isEmpty()) {
                        Optional<LineStopEntity> startEntityOpt = stopsList.stream()
                                .filter(stop -> stop.getStopName().equals(startStation) && stop.getStopOrder() == 1)
                                .findFirst();

                        if (startEntityOpt.isPresent()) {
                            LineStopEntity startEntity = startEntityOpt.get();
                            for (int i = 0; i < stopsList.size(); i++) {
                                LineStopEntity stop = stopsList.get(i);
                                if (stop.getStopName().equals(startEntity.getStopName()) &&
                                        Objects.equals(stop.getStopOrder(), startEntity.getStopOrder())) {
                                    startIndex = i;
                                    break;
                                }
                            }
                        }
                    }

                    // Determine end index if stopStation is provided
                    if (stopStation != null && !stopStation.isEmpty()) {
                        Optional<LineStopEntity> stopEntityOpt = stopsList.stream()
                                .filter(stop -> stop.getStopName().equals(stopStation))
                                .findFirst();

                        if (stopEntityOpt.isPresent()) {
                            endIndex = stopEntityOpt.get().getStopOrder();
                        }
                    }

                    // Ensure startIndex is less than endIndex
                    if (startIndex >= endIndex) {
                        log.error("The start station {} must be before the stop station {}", startStation, stopStation);
                        return Flux.empty();
                    }

                    // Return a sublist from the start to the identified endIndex (exclusive)
                    List<LineStopEntity> result = ConvertEntityToDto.getSubListOfSingleDirection(stopsList, startIndex);
                    return ConvertEntityToDto.convertLineStopToStation(Flux.fromIterable(result));
                });
    }

}
