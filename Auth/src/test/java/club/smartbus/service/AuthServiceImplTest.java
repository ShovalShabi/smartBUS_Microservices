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

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

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
