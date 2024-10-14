package club.smartbus.utils;

import club.smartbus.data.LineStopEntity;
import club.smartbus.dto.transit.LatLng;
import club.smartbus.dto.transit.Location;
import club.smartbus.dto.transit.Station;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Utility class for converting entities to DTOs.
 * This class contains helper methods for converting {@link LineStopEntity} objects into {@link Station} DTOs
 * and for extracting sublists of stations in a single direction based on stop order.
 */
@Slf4j
public class ConvertEntityToDto {

    /**
     * Converts a {@link Flux} of {@link LineStopEntity} objects to a {@link Flux} of {@link Station} DTOs.
     *
     * <p>This method maps each {@link LineStopEntity} to its corresponding {@link Station} DTO. The mapping
     * includes converting latitude and longitude values into {@link LatLng} and wrapping them in a {@link Location} object.</p>
     *
     * @param intermediateStations a {@link Flux} of {@link LineStopEntity} objects representing bus stops.
     * @return a {@link Flux<Station>} containing the mapped {@link Station} DTOs.
     */
    public static Flux<Station> convertLineStopToStation(Flux<LineStopEntity> intermediateStations) {
        return intermediateStations.map(lineStopEntity -> {
            return new Station(
                    lineStopEntity.getStopName(),
                    new Location(new LatLng(lineStopEntity.getLat(), lineStopEntity.getLng())),
                    lineStopEntity.getStopOrder()
            );
        });
    }

    /**
     * Extracts a sublist of bus stops that follow a single direction based on stop order.
     *
     * <p>This method starts from the given {@code startIndex} in the list and continues to the next stop until
     * it finds a stop where the order decreases, indicating a change in direction. It returns a sublist that
     * includes all the stops in the same direction as the starting point.</p>
     *
     * <p>If the list contains fewer than 2 stops, the original list is returned.</p>
     *
     * @param stops      a {@link List} of {@link LineStopEntity} representing bus stops along a route.
     * @param startIndex the index of the first stop in the sublist.
     * @return a sublist of stops that follow a single direction based on stop order.
     */
    public static List<LineStopEntity> getSubListOfSingleDirection(List<LineStopEntity> stops, int startIndex) {
        log.info("Start from station at index:{}", startIndex);
        if (stops == null || stops.size() <= 1)
            return stops;

        // Iterate through the list to find the first decreasing stopOrder
        int endIndex = 1 + startIndex;
        while (endIndex < stops.size() &&
                stops.get(endIndex).getStopOrder() >= stops.get(endIndex - 1).getStopOrder()) {
            endIndex++;
        }

        log.info("To station at index:{}", endIndex);

        // Return a sublist from the start to the identified endIndex (exclusive)
        return stops.subList(startIndex, endIndex);
    }
}
