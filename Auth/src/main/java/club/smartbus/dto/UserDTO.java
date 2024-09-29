package club.smartbus.dto;

import club.smartbus.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    @NotBlank(message = "Name cannot be empty")
    private String firstName;

    @NotBlank(message = "Surname cannot be empty")
    private String surname;

    @NotBlank(message = "Company cannot be empty")
    private String company;

    @Email(message = "Invalid email format")
    private String organizationEmail;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one uppercase letter, one lowercase letter, and one special character")
    private String password;

    private Role role;
}
