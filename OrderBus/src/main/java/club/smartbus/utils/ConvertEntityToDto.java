package club.smartbus.utils;

import club.smartbus.data.LineStopEntity;
import club.smartbus.dto.transit.LatLng;
import club.smartbus.dto.transit.Location;
import club.smartbus.dto.transit.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;

public class ConvertEntityToDto {
    private static final Logger log = LoggerFactory.getLogger(ConvertEntityToDto.class);

    public static Flux<Station> convertLineStopToStation(Flux<LineStopEntity> intermediateStations) {
        return intermediateStations.map(lineStopEntity -> {
            return new Station(
                    lineStopEntity.getStopName(),
                    new Location(new LatLng(lineStopEntity.getLat(), lineStopEntity.getLng())),
                    lineStopEntity.getStopOrder()
            );
        });
    }

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
