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

@EnableAutoConfiguration
@Table("users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Column("first_name")
    private String firstName;

    @Column("surname")
    private String surname;

    @Column("company")
    private String company;

    @Id
    @Column("organization_email")
    private String organizationEmail;

    @Column("password")
    private String password;

    @Column("role")
    private Role role;

    // Constructor
    public UserEntity(UserDTO userDTO) {
        firstName = userDTO.getFirstName();
        surname = userDTO.getSurname();
        company = userDTO.getCompany();
        organizationEmail = userDTO.getOrganizationEmail();
        password = userDTO.getPassword();
        role = userDTO.getRole();
    }

    // Convert to DTO
    public UserDTO toDTO() {
        return new UserDTO(firstName,
                surname,
                company,
                organizationEmail,
                password,
                role);
    }
}

