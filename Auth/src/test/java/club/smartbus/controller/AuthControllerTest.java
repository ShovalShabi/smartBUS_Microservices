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

/**
 * Integration test class for {@link AuthController}.
 * <p>
 * This class tests the functionality of the AuthController by simulating HTTP requests
 * and verifying the responses. It mocks the {@link AuthService} and {@link UserRepository}
 * to test the controller's behavior independently of the actual service and repository implementations.
 * <p>
 * The security is disabled in the test environment using the {@link TestSecurityConfig} class to allow
 * for testing without requiring authentication.
 */
@WebFluxTest(controllers = AuthController.class)
@Import(TestSecurityConfig.class)  // Ensure we import security config to bypass security in tests
public class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;  // Mock UserRepository

    @Autowired
    private WebTestClient webTestClient;

    /**
     * Initializes the mocks before each test method is executed.
     * This method ensures that Mockito is ready to handle the test interactions.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for successfully registering a user.
     * <p>
     * This test simulates a POST request to the registration endpoint and verifies that
     * the user is correctly registered. It checks that the response contains the expected
     * user information and ensures that the service method is called with the appropriate parameters.
     */
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

    /**
     * Test case for successfully logging in a user.
     * <p>
     * This test simulates a POST request to the login endpoint and verifies that the
     * user is successfully authenticated. It checks that the response contains the expected
     * JWT token and ensures that the service method is called with the correct parameters.
     */
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

    /**
     * Test case for successfully deleting all users.
     * <p>
     * This test simulates a DELETE request to the endpoint that deletes all users.
     * It checks that the response confirms the deletion and verifies that the service method
     * responsible for deleting users is called.
     */
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
