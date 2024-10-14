package club.smartbus.service;

import club.smartbus.dto.stations.StationsRequestDTO;
import club.smartbus.dto.transit.LatLng;
import club.smartbus.dto.transit.Station;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface representing the service responsible for handling bus-related operations.
 * This includes retrieving bus line stations, finding relevant bus lines for a specific route,
 * and identifying station names by geographic coordinates.
 */
public interface BusService {

    /**
     * Retrieves a paginated list of stations for a specific bus line based on the provided request.
     * The list can be filtered by the start and stop station names.
     *
     * @param stationsRequestDTO The request containing the bus line number and agency information.
     * @param startStation    Optional parameter to filter the results by a starting station.
     * @param stopStation     Optional parameter to filter the results by a stop station.
     * @param size            The number of results to retrieve per page.
     * @param page            The page number to retrieve.
     * @return A reactive stream (Flux) of {@link Station} objects representing the bus line stations.
     */
    Flux<Station> getBusLineStations(StationsRequestDTO stationsRequestDTO, String startStation, String stopStation, int size, int page);

    /**
     * Finds relevant bus lines that pass through both the start and end locations.
     * This is used to identify bus lines that can be used for a passenger's requested journey.
     *
     * @param startLocation The geographical coordinates (latitude and longitude) of the starting location.
     * @param endLocation   The geographical coordinates (latitude and longitude) of the destination location.
     * @return A reactive stream (Flux) of bus line numbers that pass through both the start and end locations.
     */
    Flux<String> getRelevantBusLineByStartAndDestinationLocation(LatLng startLocation, LatLng endLocation);

    /**
     * Finds the name of a station based on its latitude and longitude coordinates.
     * This is useful for identifying a station by its geographic location.
     *
     * @param coordinate The geographical coordinates (latitude and longitude) of the station.
     * @return A {@link Mono} containing the station name if found, or an empty Mono if no match is found.
     */
    Mono<String> findStationNameByLatitudeAndLongitude(LatLng coordinate);
}
