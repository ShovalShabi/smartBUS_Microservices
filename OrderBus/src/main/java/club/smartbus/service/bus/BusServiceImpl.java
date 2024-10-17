package club.smartbus.service.bus;

import club.smartbus.dto.stations.StationsRequestDTO;
import club.smartbus.dal.LineStopRepository;
import club.smartbus.data.LineStopEntity;
import club.smartbus.dto.transit.LatLng;
import club.smartbus.dto.transit.Station;
import club.smartbus.utils.ConvertEntityToDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
     * @param stationsRequestDTO the line number and agency name of the bus route.
     * @param startStation    the name of the start station (can be empty).
     * @param stopStation     the name of the stop station (can be empty).
     * @param size            the number of stations to return per page.
     * @param page            the page number to retrieve.
     * @return a {@link Flux<Station>} containing the paginated list of stations between the start and stop stations.
     * @throws RuntimeException         if no stations are found for the provided line number or if the start/stop station is not found.
     * @throws IllegalArgumentException if the start station is after the stop station.
     */
    @Override
    public Flux<Station> getBusLineStations(StationsRequestDTO stationsRequestDTO, String startStation, String stopStation, int size, int page) {
        // Fetch all stations for the line number
        return lineStopRepository.findLineStopEntitiesByLineNumber(stationsRequestDTO.getLineNumber())
                .collectList()
                .flatMapMany(stopsList -> {
                    if (stopsList.isEmpty()) {
                        // If no stops are found, return a Flux error
                        return Flux.error(new RuntimeException("No stations found for line number '" + stationsRequestDTO.getLineNumber() + "'"));
                    }

                    // Filter for stops of the requested agency
                    stopsList = stopsList.stream()
                            .filter(station -> station.getAgencyName().equals(stationsRequestDTO.getAgency()))
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


    /**
     * Retrieves the bus lines of agencies that operate buses passing through both the start and end locations.
     *
     * <p>This method cross-checks the bus stops at the start location and the end location and returns the line numbers
     * of the agencies that pass through both stops.</p>
     *
     * @param startLocation The geographical location (latitude, longitude) of the start bus stop.
     * @param endLocation   The geographical location (latitude, longitude) of the destination bus stop.
     * @return A Flux<String> containing the line numbers of the agencies that have buses passing through both the start and end locations.
     */
    @Override
    public Flux<String> getRelevantBusLineByStartAndDestinationLocation(LatLng startLocation, LatLng endLocation) {

        // Step 1: Find all agencies that have a stop at the start location using latitude and longitude.
        // This returns a Flux of LineStopEntity objects representing bus stops at the start location.
        Flux<LineStopEntity> startStationAgencies = lineStopRepository.findByLineNumbersByStationLocation(
                startLocation.getLatitude(), startLocation.getLongitude());

        // Step 2: Find all agencies that have a stop at the end location using latitude and longitude.
        // This returns a Flux of LineStopEntity objects representing bus stops at the end location.
        Flux<LineStopEntity> endStationAgencies = lineStopRepository.findByLineNumbersByStationLocation(
                endLocation.getLatitude(), endLocation.getLongitude());

        // Step 3: Compare agencies from the start and end locations.
        // For each start station, check if any end station belongs to the same agency.
        // Map the matching agency name and ensure no duplicate names are returned using 'distinct()'.
        return startStationAgencies
                .flatMap(startStation -> endStationAgencies
                        .filter(endStation -> startStation.getLineNumber().equals(endStation.getLineNumber()))
                        .map(LineStopEntity::getLineNumber)) // Map the agency name from the matching start and end stations.
                .distinct(); // Ensure no duplicate agency names are returned.
    }

    /**
     * Finds the name of a station based on its latitude and longitude coordinates.
     * This is useful for identifying a station by its geographic location.
     *
     * @param coordinate The geographical coordinates (latitude and longitude) of the station.
     * @return A {@link Mono<String>} containing the station name if found, or an empty Mono if no match is found.
     */
    @Override
    public Mono<String> findStationNameByLatitudeAndLongitude(LatLng coordinate) {
        return lineStopRepository.findStationNameByLatitudeAndLongitude(coordinate.getLatitude(), coordinate.getLongitude());
    }


}
