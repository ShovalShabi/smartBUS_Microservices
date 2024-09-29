package club.smartbus.etc;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class responsible for initializing and encoding the admin user's password at startup.
 * <p>
 * This class reads the admin password from the application properties and encodes it using the provided
 * {@link PasswordEncoder}. The encoded password is stored and can be retrieved for security purposes.
 * </p>
 */
@Slf4j
@Configuration
@Data
public class StartupConfig {

    private final PasswordEncoder passwordEncoder;
    private String encodedPassword;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    /**
     * Constructor for {@link StartupConfig} that injects the {@link PasswordEncoder}.
     *
     * @param passwordEncoder The encoder used to encode the admin password.
     */
    public StartupConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method annotated with {@link PostConstruct} to encode the admin password after the bean is created.
     * <p>
     * This method checks if the admin password is provided through the application properties. If it is,
     * the password is encoded and logged. If no password is provided, it logs an error.
     * </p>
     */
    @PostConstruct
    public void encodePassword() {
        if (adminPassword != null) {
            this.encodedPassword = passwordEncoder.encode(adminPassword);
            log.info("Generated BCrypt Password for admin: " + encodedPassword);
        } else {
            log.error("Admin password not provided");
        }
    }

    /**
     * Retrieves the encoded admin password.
     * <p>
     * If the password is not encoded yet, it logs an error.
     * </p>
     *
     * @return the encoded admin password or logs an error if it is null.
     */
    public String getEncodedPassword() {
        if (encodedPassword == null) {
            log.error("Encoded password is null!");
        }
        return encodedPassword;
    }
}
