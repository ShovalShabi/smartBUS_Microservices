package club.smartbus.utils;

import club.smartbus.dto.polyline.LatLng;
import club.smartbus.dto.transit.TransitDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

public class ZipMonoOfLists {
    public static Mono<List<Tuple2<List<TransitDetails>, List<LatLng>>>> zipMonoListOfLists(Mono<List<List<TransitDetails>>> mono1, Mono<List<List<LatLng>>> mono2) {
        return mono1.flatMapMany(Flux::fromIterable)
                .zipWith(mono2.flatMapMany(Flux::fromIterable))
                .collectList();
    }
}
