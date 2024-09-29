package club.smartbus.service;

import club.smartbus.dal.UserRepository;
import club.smartbus.data.UserEntity;
import club.smartbus.dto.UserDTO;
import club.smartbus.utils.JWTUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link AuthServiceImpl}.
 * <p>
 * This class contains unit tests for the {@link AuthServiceImpl} service methods, focusing on testing
 * the registration, login, and user deletion functionalities. The tests use mock objects for dependencies
 * such as {@link UserRepository}, {@link PasswordEncoder}, and {@link JWTUtil}.
 */
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private AutoCloseable closeable;

    /**
     * Initializes the mocks before each test method execution.
     * This ensures that Mockito is set up properly for each test case.
     */
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Closes the resources after each test method execution.
     *
     * @throws Exception if an exception occurs while closing resources.
     */
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Test case for successfully registering a new user.
     * <p>
     * It checks that the user is correctly registered when no existing user is found,
     * the password is encoded, and the user entity is inserted into the database.
     */
    @Test
    void testRegisterUser_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO("John", "Doe", "Company", "john.doe@domain.com", "password", null);
        UserEntity userEntity = new UserEntity(userDTO);

        when(userRepository.findById(anyString())).thenReturn(Mono.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.insertUser(any(UserEntity.class))).thenReturn(Mono.just(userEntity));

        // Act
        Mono<UserDTO> result = authService.registerUser("Company", userDTO);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getOrganizationEmail().equals("john.doe@domain.com"))
                .verifyComplete();

        verify(userRepository).findById(anyString());
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).insertUser(any(UserEntity.class));
    }

    /**
     * Test case for handling the scenario when the user already exists.
     * <p>
     * It verifies that the existing user is returned without attempting to insert a new one.
     */
    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Arrange
        UserDTO userDTO = new UserDTO("John", "Doe", "Company", "john.doe@domain.com", "password", null);
        UserEntity existingUser = new UserEntity(userDTO);

        when(userRepository.findById(anyString())).thenReturn(Mono.just(existingUser));

        // Act
        Mono<UserDTO> result = authService.registerUser("Company", userDTO);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getOrganizationEmail().equals("john.doe@domain.com"))
                .verifyComplete();

        verify(userRepository).findById(anyString());
        verify(userRepository, never()).insertUser(any(UserEntity.class));
    }

    /**
     * Test case for successfully logging in a user with a new JWT token.
     * <p>
     * It checks that the password is correctly validated, and if no token exists, a new token is generated.
     */
    @Test
    void testUserLogin_SuccessWithNewToken() {
        // Arrange
        UserEntity userEntity = new UserEntity("John", "Doe", "Company", "john.doe@domain.com", "encodedPassword", null);

        when(userRepository.findById(anyString())).thenReturn(Mono.just(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.getTokenForUser(anyString())).thenReturn(null);
        when(jwtUtil.generateToken(anyString())).thenReturn("newToken");

        // Act
        Mono<UserDTO> result = authService.userLogin("Company", "john.doe@domain.com", "password");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getPassword().equals("newToken"))
                .verifyComplete();

        verify(userRepository).findById(anyString());
        verify(jwtUtil).generateToken(anyString());
    }

    /**
     * Test case for handling failed login due to invalid credentials.
     * <p>
     * It verifies that when the password does not match, an error is returned indicating invalid credentials.
     */
    @Test
    void testUserLogin_Failure_InvalidCredentials() {
        // Arrange
        UserEntity userEntity = new UserEntity("John", "Doe", "Company", "john.doe@domain.com", "encodedPassword", null);

        when(userRepository.findById(anyString())).thenReturn(Mono.just(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act
        Mono<UserDTO> result = authService.userLogin("Company", "john.doe@domain.com", "wrongPassword");

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Invalid credentials"))
                .verify();

        verify(userRepository).findById(anyString());
        verify(passwordEncoder).matches(anyString(), anyString());
    }

    /**
     * Test case for successfully deleting all users.
     * <p>
     * It verifies that the service correctly deletes all users from the database.
     */
    @Test
    void testDeleteAllUsers_Success() {
        // Arrange
        when(userRepository.deleteAll()).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = authService.deleteAllUsers();

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepository).deleteAll();
    }
}
