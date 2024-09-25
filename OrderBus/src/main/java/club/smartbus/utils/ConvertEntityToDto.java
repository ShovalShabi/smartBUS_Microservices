package club.smartbus.utils;

import club.smartbus.data.LineStopEntity;
import club.smartbus.dto.transit.LatLng;
import club.smartbus.dto.transit.Location;
import club.smartbus.dto.transit.Station;
import reactor.core.publisher.Flux;

public class ConvertEntityToDto {
    public static Flux<Station> convertLineStopToStation(Flux<LineStopEntity> intermediateStations) {
        return intermediateStations.map(lineStopEntity -> {
            return new Station(
                    lineStopEntity.getStopName(),
                    new Location(new LatLng(lineStopEntity.getLat(), lineStopEntity.getLng()))
            );
        });
    }
}
