package club.smartbus.service;

import club.smartbus.dal.UserRepository;
import club.smartbus.data.UserEntity;
import club.smartbus.dto.UserDTO;
import club.smartbus.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public Mono<UserDTO> registerUser(String company, @Valid UserDTO userDTO) {
        return userRepository.findById(userDTO.getOrganizationEmail())
                .flatMap(existingUser -> {
                    // If the user is found, return it as a DTO
                    log.info("The user {} already exists", userDTO.getOrganizationEmail());
                    return Mono.just(existingUser.toDTO());
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // If the user is not found, create a new user and save it

                    // Log and ensure the password is not null
                    if (userDTO.getPassword() == null) {
                        log.error("Password for user {} is null!", userDTO.getOrganizationEmail());
                        return Mono.error(new IllegalArgumentException("Password cannot be null"));
                    }

                    // Encrypt the password
                    String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
                    log.info("Encrypted the password for user {} successfully: {}", userDTO.getOrganizationEmail(), encodedPassword);

                    // Set the encoded password to the user DTO
                    userDTO.setPassword(encodedPassword);

                    // Set the company name to the user DTO
                    userDTO.setCompany(company);

                    // Save the user to the database
                    UserEntity newUser = new UserEntity(userDTO);

                    return userRepository.insertUser(newUser)
                            .map(UserEntity::toDTO);  // Map the saved entity to DTO
                }).doOnSuccess(savedUser -> log.info("The user {} has been saved successfully for company: {}",
                        userDTO.getOrganizationEmail(), company)));
    }

    @Override
    public Mono<UserDTO> userLogin(String company, String email, String password) {
        return userRepository.findById(email)
                .flatMap(userEntity -> {
                    // Step 1: Check if the password matches
                    if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                        log.warn("Login attempt failed for user: {} in company: {}. Invalid credentials.", email, company);
                        return Mono.error(new RuntimeException("Invalid credentials"));
                    }

                    // Step 2: If the user exists and password is correct, handle the JWT token
                    String currentToken = jwtUtil.getTokenForUser(email);

                    if (currentToken == null || !jwtUtil.validateToken(currentToken)) {
                        // No valid token exists, generate a new one
                        String newToken = jwtUtil.generateToken(email);
                        log.info("Generated new JWT token for user: {}", email);

                        // Return the userDTO with the new token
                        UserDTO userDTO = userEntity.toDTO();
                        userDTO.setPassword(newToken);  // Attach the JWT token to the response
                        log.info("User login successful. Returning new token for user: {}", email);
                        return Mono.just(userDTO);
                    } else if (jwtUtil.isTokenExpired(currentToken)) {
                        // Token exists but is expired, renew the token
                        String renewedToken = jwtUtil.renewToken(currentToken);
                        log.info("Renewed expired JWT token for user: {}", email);

                        // Return the userDTO with the renewed token
                        UserDTO userDTO = userEntity.toDTO();
                        userDTO.setPassword(renewedToken);  // Attach the renewed token to the response
                        log.info("User login successful. Returning renewed token for user: {}", email);
                        return Mono.just(userDTO);
                    } else {
                        // Valid token exists, no need to renew
                        log.info("User login successful. Valid JWT token exists for user: {}", email);
                        UserDTO userDTO = userEntity.toDTO();
                        userDTO.setPassword(currentToken);  // Attach the existing valid token to the response
                        return Mono.just(userDTO);
                    }
                })
                // Step 1: If user is not found, log and return an error
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Login attempt failed for user: {} in company: {}. User not registered.", email, company);
                    return Mono.error(new RuntimeException("User not found"));
                }));
    }


}
