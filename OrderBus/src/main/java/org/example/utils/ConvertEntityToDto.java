package org.example.utils;

import org.example.data.LineStopEntity;
import org.example.dto.transit.LatLng;
import org.example.dto.transit.Location;
import org.example.dto.transit.Station;
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
