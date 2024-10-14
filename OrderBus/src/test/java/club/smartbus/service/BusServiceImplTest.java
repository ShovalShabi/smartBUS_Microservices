package club.smartbus.service;

import club.smartbus.dal.LineStopRepository;
import club.smartbus.data.LineStopEntity;
import club.smartbus.dto.stations.StationsRequestDTO;
import club.smartbus.dto.transit.LatLng;
import club.smartbus.dto.transit.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link BusServiceImpl}.
 * This class tests various bus-related functionalities like retrieving bus line stations,
 * finding relevant bus lines, and retrieving station names.
 */
class BusServiceImplTest {

    @Mock
    private LineStopRepository lineStopRepository;

    @InjectMocks
    private BusServiceImpl busService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the successful retrieval of bus line stations with start and stop station provided.
     * The test uses the specific station data provided.
     */
    @Test
    void testGetBusLineStations_Success_WithStartAndStopStations() {
        // Arrange
        LineStopEntity stop1 = new LineStopEntity("1", "ת. רכבת יבנה מערב", 1, 31.8907, 34.731191, "אלקטרה אפיקים");
        LineStopEntity stop2 = new LineStopEntity("1", "ת. רכבת יבנה מזרח", 19, 31.862017, 34.744082, "אלקטרה אפיקים");
        List<LineStopEntity> stops = Arrays.asList(stop1, stop2);

        when(lineStopRepository.findLineStopEntitiesByLineNumber("1")).thenReturn(Flux.fromIterable(stops));

        // Act
        Flux<Station> result = busService.getBusLineStations(
                new StationsRequestDTO("1", "אלקטרה אפיקים"),
                "ת. רכבת יבנה מערב",
                "ת. רכבת יבנה מזרח",
                10,
                0
        );

        // Assert
        StepVerifier.create(result)
                .expectNextCount(2)  // Expect 2 stations in the result
                .verifyComplete();

        verify(lineStopRepository).findLineStopEntitiesByLineNumber("1");
    }

    /**
     * Tests failure when no bus stops are found for the provided line number.
     */
    @Test
    void testGetBusLineStations_Failure_NoStopsFound() {
        // Arrange: Empty result from the repository
        when(lineStopRepository.findLineStopEntitiesByLineNumber("invalidLine"))
                .thenReturn(Flux.empty());

        // Act: Call the service method
        Flux<Station> result = busService.getBusLineStations(
                new StationsRequestDTO("invalidLine", "אלקטרה אפיקים"),
                null,
                null,
                10,
                0
        );

        // Assert: Expect a runtime exception for no stations found
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(lineStopRepository).findLineStopEntitiesByLineNumber("invalidLine");
    }

    /**
     * Tests the successful retrieval of relevant bus lines between two locations.
     * The test uses the specific station data provided.
     */
    @Test
    void testGetRelevantBusLineByStartAndDestinationLocation_Success() {
        // Arrange
        LineStopEntity startStop = new LineStopEntity("1", "ת. רכבת יבנה מערב", 1, 31.8907, 34.731191, "אלקטרה אפיקים");
        LineStopEntity endStop = new LineStopEntity("1", "ת. רכבת יבנה מזרח", 19, 31.862017, 34.744082, "אלקטרה אפיקים");

        when(lineStopRepository.findByLineNumbersByStationLocation(31.8907, 34.731191))
                .thenReturn(Flux.just(startStop));
        when(lineStopRepository.findByLineNumbersByStationLocation(31.862017, 34.744082))
                .thenReturn(Flux.just(endStop));

        // Act
        Flux<String> result = busService.getRelevantBusLineByStartAndDestinationLocation(
                new LatLng(31.8907, 34.731191),
                new LatLng(31.862017, 34.744082)
        );

        // Assert
        StepVerifier.create(result)
                .expectNext("1")
                .verifyComplete();

        verify(lineStopRepository).findByLineNumbersByStationLocation(31.8907, 34.731191);
        verify(lineStopRepository).findByLineNumbersByStationLocation(31.862017, 34.744082);
    }

    /**
     * Tests failure when no relevant bus lines are found between two locations.
     */
    @Test
    void testGetRelevantBusLineByStartAndDestinationLocation_Failure_NoMatchFound() {
        // Arrange
        when(lineStopRepository.findByLineNumbersByStationLocation(anyDouble(), anyDouble()))
                .thenReturn(Flux.empty());

        // Act: Call the service method
        Flux<String> result = busService.getRelevantBusLineByStartAndDestinationLocation(
                new LatLng(31.8907, 34.731191),
                new LatLng(31.862017, 34.744082)
        );

        // Assert: Expect an empty Flux as no bus lines match both locations
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(lineStopRepository, times(2)).findByLineNumbersByStationLocation(anyDouble(), anyDouble());
    }

    /**
     * Tests the successful retrieval of a station name by its coordinates.
     */
    @Test
    void testFindStationNameByLatitudeAndLongitude_Success() {
        // Arrange
        when(lineStopRepository.findStationNameByLatitudeAndLongitude(31.8907, 34.731191))
                .thenReturn(Mono.just("ת. רכבת יבנה מערב"));

        // Act
        Mono<String> result = busService.findStationNameByLatitudeAndLongitude(new LatLng(31.8907, 34.731191));

        // Assert
        StepVerifier.create(result)
                .expectNext("ת. רכבת יבנה מערב")
                .verifyComplete();

        verify(lineStopRepository).findStationNameByLatitudeAndLongitude(31.8907, 34.731191);
    }

    /**
     * Tests failure when no station is found for the provided coordinates.
     */
    @Test
    void testFindStationNameByLatitudeAndLongitude_Failure_NoStationFound() {
        // Arrange: Empty Mono result from the repository
        when(lineStopRepository.findStationNameByLatitudeAndLongitude(31.8907, 34.731191))
                .thenReturn(Mono.empty());

        // Act: Call the service method
        Mono<String> result = busService.findStationNameByLatitudeAndLongitude(new LatLng(31.8907, 34.731191));

        // Assert: Expect the Mono to complete without any value
        StepVerifier.create(result)
                .verifyComplete();

        verify(lineStopRepository).findStationNameByLatitudeAndLongitude(31.8907, 34.731191);
    }
}
