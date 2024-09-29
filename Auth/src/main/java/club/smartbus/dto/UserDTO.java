package club.smartbus.dto;

import club.smartbus.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

/**
 * Data Transfer Object (DTO) representing user data.
 * <p>
 * This class is used for transferring user-related data between different layers, typically between the client and the server.
 * It includes user information such as first name, surname, company, email, password, and role.
 * </p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    /**
     * The first name of the user.
     * <p>
     * This field is mandatory and cannot be empty.
     * </p>
     */
    @NotBlank(message = "Name cannot be empty")
    private String firstName;

    /**
     * The surname (last name) of the user.
     * <p>
     * This field is mandatory and cannot be empty.
     * </p>
     */
    @NotBlank(message = "Surname cannot be empty")
    private String surname;

    /**
     * The company associated with the user.
     * <p>
     * This field is mandatory and cannot be empty.
     * </p>
     */
    @NotBlank(message = "Company cannot be empty")
    private String company;

    /**
     * The unique email of the user.
     * <p>
     * This field must be a valid email format and cannot be empty.
     * </p>
     */
    @Email(message = "Invalid email format")
    private String organizationEmail;

    /**
     * The password for the user.
     * <p>
     * This field is mandatory, with the following constraints:
     * <ul>
     *     <li>Password length must be between 6 and 12 characters.</li>
     *     <li>Password must contain at least one digit, one uppercase letter, one lowercase letter, and one special character.</li>
     * </ul>
     * </p>
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one uppercase letter, one lowercase letter, and one special character")
    private String password;

    /**
     * The role of the user, represented by the {@link Role} enum.
     * <p>
     * This field is optional and can be used to assign a specific role to the user (e.g., ADMIN, USER).
     * </p>
     */
    private Role role;
}
