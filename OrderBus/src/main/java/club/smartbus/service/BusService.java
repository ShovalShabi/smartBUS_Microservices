package club.smartbus.service;

import club.smartbus.boundaries.stations.StationsRequest;
import club.smartbus.dto.transit.Station;
import reactor.core.publisher.Flux;

public interface BusService {
    Flux<Station> getBusLineStations(StationsRequest stationsRequest, String startStation, String stopStation, int size, int page);
}
