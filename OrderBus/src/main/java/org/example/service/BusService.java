package org.example.service;

import org.example.dto.transit.Station;
import reactor.core.publisher.Flux;

public interface BusService {
    Flux<Station> getBusLineStations(String lineNumber, String startStation, String stopStation, int size, int page);
}
