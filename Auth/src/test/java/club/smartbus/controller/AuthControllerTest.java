package club.smartbus.controller;

import club.smartbus.dto.UserDTO;
import club.smartbus.service.AuthService;
import club.smartbus.dal.UserRepository;
import club.smartbus.etc.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = AuthController.class)
@Import(TestSecurityConfig.class)  // Ensure we import security config to bypass security in tests
public class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;  // Mock UserRepository

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO("John", "Doe", "Company", "john.doe@domain.com", "password", null);

        when(authService.registerUser(anyString(), any(UserDTO.class))).thenReturn(Mono.just(userDTO));

        // Act and Assert
        webTestClient.post()
                .uri("/register/Company")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(response -> {
                    assert response.getOrganizationEmail().equals("john.doe@domain.com");
                });

        verify(authService).registerUser(anyString(), any(UserDTO.class));
    }

    @Test
    void testUserLogin_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO("John", "Doe", "Company", "john.doe@domain.com", "token", null);

        when(authService.userLogin(anyString(), anyString(), anyString())).thenReturn(Mono.just(userDTO));

        // Act and Assert
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/Company")
                        .queryParam("email", "john.doe@domain.com")
                        .queryParam("password", "password")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .value(response -> {
                    assert response.getPassword().equals("token");
                });

        verify(authService).userLogin(anyString(), anyString(), anyString());
    }

    @Test
    void testDeleteAllUsers_Success() {
        // Arrange
        when(authService.deleteAllUsers()).thenReturn(Mono.empty());

        // Act and Assert
        webTestClient.delete()
                .uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> {
                    assert response.equals("All users deleted successfully");
                });

        verify(authService).deleteAllUsers();
    }
}
