package club.smartbus.data;

import club.smartbus.dto.UserDTO;
import club.smartbus.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Entity class representing the 'users' table in the database.
 * <p>
 * This class holds the user's data including first name, surname, company, organization email, password, and role.
 * </p>
 * <p>
 * It also provides utility methods to convert from and to {@link UserDTO}.
 * </p>
 */
@EnableAutoConfiguration
@Table("users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    /**
     * The first name of the user.
     */
    @Column("first_name")
    private String firstName;

    /**
     * The surname (last name) of the user.
     */
    @Column("surname")
    private String surname;

    /**
     * The company associated with the user.
     */
    @Column("company")
    private String company;

    /**
     * The unique email of the user, used as the primary identifier.
     */
    @Id
    @Column("organization_email")
    private String organizationEmail;

    /**
     * The password of the user, which is typically encrypted.
     */
    @Column("password")
    private String password;

    /**
     * The role of the user (e.g., ADMIN, USER), represented by the {@link Role} enum.
     */
    @Column("role")
    private Role role;

    /**
     * Constructs a {@link UserEntity} object from a {@link UserDTO}.
     * <p>
     * This constructor is used to create an entity from a data transfer object.
     * </p>
     *
     * @param userDTO The {@link UserDTO} containing user information.
     */
    public UserEntity(UserDTO userDTO) {
        firstName = userDTO.getFirstName();
        surname = userDTO.getSurname();
        company = userDTO.getCompany();
        organizationEmail = userDTO.getOrganizationEmail();
        password = userDTO.getPassword();
        role = userDTO.getRole();
    }

    /**
     * Converts this {@link UserEntity} object to a {@link UserDTO}.
     * <p>
     * This method is used to convert the entity object into a data transfer object for external communication.
     * </p>
     *
     * @return A {@link UserDTO} containing the user's information.
     */
    public UserDTO toDTO() {
        return new UserDTO(firstName,
                surname,
                company,
                organizationEmail,
                password,
                role);
    }
}
