package club.smartbus.etc;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@Data
public class StartupConfig {

    private final PasswordEncoder passwordEncoder;
    private String encodedPassword;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    public StartupConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Encode the password during initialization
    @PostConstruct
    public void encodePassword() {
        if (adminPassword != null) {
            this.encodedPassword = passwordEncoder.encode(adminPassword);
            log.info("Generated BCrypt Password for admin: " + encodedPassword);
        } else {
            log.error("Admin password not provided");
        }
    }

    public String getEncodedPassword() {
        if (encodedPassword == null) {
            log.error("Encoded password is null!");
        }
        return encodedPassword;
    }
}
