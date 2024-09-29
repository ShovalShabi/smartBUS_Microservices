package club.smartbus.controller;

import club.smartbus.dto.UserDTO;
import club.smartbus.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST controller for handling user authentication and registration requests.
 * This controller provides endpoints for user registration and login for a specific company.
 */
@Slf4j
@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    /**
     * Registers a new user for the specified company.
     *
     * @param company The company for which the user is registering.
     * @param userDTO The {@link UserDTO} containing user registration data.
     * @return A {@link Mono} emitting the registered {@link UserDTO} if successful.
     */
    @PostMapping(
            path = {"/register/{company}"},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<UserDTO> registerUser(@PathVariable String company,
                                      @RequestBody UserDTO userDTO) {
        log.info("Registering user for company: {}, with data: {}", company, userDTO);
        return authService.registerUser(company, userDTO);
    }

    /**
     * Authenticates a user for the specified company.
     *
     * @param company  The company to which the user belongs.
     * @param email    The email of the user trying to log in.
     * @param password The password of the user trying to log in.
     * @return A {@link Mono} emitting the authenticated {@link UserDTO} with a JWT token if successful.
     */
    @PostMapping(
            path = {"/auth/{company}"},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<UserDTO> userLogin(@PathVariable String company,
                                   @RequestParam String email,
                                   @RequestParam String password) {
        log.info("Login attempt for user: {}, for company: {}", email, company);
        return authService.userLogin(company, email, password);
    }

}