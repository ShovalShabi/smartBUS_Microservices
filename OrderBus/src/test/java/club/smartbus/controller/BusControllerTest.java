package club.smartbus.controller;

import club.smartbus.dto.stations.StationsRequestDTO;
import club.smartbus.dto.transit.Station;
import club.smartbus.etc.TestSecurityConfig;
import club.smartbus.service.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link BusController}.
 * Tests the behavior of bus-related APIs using WebFlux.
 */
@WebFluxTest(controllers = BusController.class)
@Import(TestSecurityConfig.class)
public class BusControllerTest {

    @MockBean
    private BusService busService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the retrieval of all stops for a bus line by making a POST request to the "/bus/stations" endpoint.
     * Verifies the response stream contains the expected bus stations.
     */
    @Test
    void testGetAllStops() {
        // Arrange
        Station stationA = new Station("ת. רכבת יבנה מערב", null, 1);
        Station stationB = new Station("ת. רכבת יבנה מזרח", null, 19);

        StationsRequestDTO stationsRequestDTO = new StationsRequestDTO("1", "אלקטרה אפיקים");

        when(busService.getBusLineStations(any(StationsRequestDTO.class), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Flux.fromIterable(Arrays.asList(stationA, stationB)));

        // Act
        webTestClient.post()
                .uri("/bus/stations?size=10&page=0&startStation=ת. רכבת יבנה מערב&stopStation=ת. רכבת יבנה מזרח")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(stationsRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)
                .returnResult(Station.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(station -> {
                    assertNotNull(station);
                    assertEquals("ת. רכבת יבנה מערב", station.getStationName());
                })
                .consumeNextWith(station -> {
                    assertNotNull(station);
                    assertEquals("ת. רכבת יבנה מזרח", station.getStationName());
                })
                .verifyComplete();

        // Assert that the service method was called with the correct parameters
        verify(busService).getBusLineStations(any(StationsRequestDTO.class), anyString(), anyString(), anyInt(), anyInt());
    }

    /**
     * Tests the case where no bus stations are found for the provided line and agency.
     */
    @Test
    void testGetAllStops_NoStopsFound() {
        // Arrange
        StationsRequestDTO stationsRequestDTO = new StationsRequestDTO("1", "אלקטרה אפיקים");

        when(busService.getBusLineStations(any(StationsRequestDTO.class), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Flux.empty());

        // Act
        webTestClient.post()
                .uri("/bus/stations?size=10&page=0")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(stationsRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)
                .returnResult(Station.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();

        // Assert that the service method was called with the correct parameters
        verify(busService).getBusLineStations(any(StationsRequestDTO.class), anyString(), anyString(), anyInt(), anyInt());
    }

    /**
     * Tests failure when invalid parameters are provided.
     * For example, startStation must be before stopStation.
     */
    @Test
    void testGetAllStops_InvalidParams() {
        // Arrange
        StationsRequestDTO stationsRequestDTO = new StationsRequestDTO("1", "אלקטרה אפיקים");

        // Simulate a scenario where the service returns an error due to invalid parameters.
        when(busService.getBusLineStations(any(StationsRequestDTO.class), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Flux.error(new IllegalArgumentException("Invalid start and stop station order")));

        // Act & Assert
        webTestClient.post()
                .uri("/bus/stations?size=10&page=0&startStation=InvalidStart&stopStation=InvalidStop")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(stationsRequestDTO)
                .exchange()
                .expectStatus().is5xxServerError();

        // Assert that the service method was called
        verify(busService).getBusLineStations(any(StationsRequestDTO.class), anyString(), anyString(), anyInt(), anyInt());
    }
}
