package club.smartbus.service;

import club.smartbus.dto.UserDTO;
import reactor.core.publisher.Mono;

/**
 * Interface defining the authentication-related services for the SmartBus application.
 * <p>
 * This interface provides methods for user registration and user login. The service is implemented in a reactive
 * manner using Project Reactor's {@link Mono}.
 * </p>
 */
public interface AuthService {

    /**
     * Registers a new user for a given company.
     * <p>
     * This method handles the registration process for a new user by checking if the user already exists, encrypting
     * the password, and saving the user details into the database. If the user already exists, it returns the existing
     * user as a {@link UserDTO}.
     * </p>
     *
     * @param company the name of the company to which the user belongs.
     * @param userDTO the user data transfer object containing user details.
     * @return a {@link Mono} emitting the registered user details as a {@link UserDTO} or an error if registration fails.
     */
    Mono<UserDTO> registerUser(String company, UserDTO userDTO);

    /**
     * Handles user login by validating credentials.
     * <p>
     * This method verifies the provided email and password for the user, checks if the user is registered, and
     * generates or renews a JWT token if the credentials are valid. If the credentials are invalid or the user
     * is not found, it emits an error.
     * </p>
     *
     * @param email    the email of the user trying to log in.
     * @param password the password of the user trying to log in.
     * @return a {@link Mono} emitting the user details along with a JWT token as a {@link UserDTO} or an error if login fails.
     */
    Mono<UserDTO> userLogin(String email, String password);

    /**
     * Deletes all users in the database for testing purposes.
     *
     * @return A {@link Mono} signaling the completion of the operation.
     */
    Mono<Void> deleteAllUsers();
}
