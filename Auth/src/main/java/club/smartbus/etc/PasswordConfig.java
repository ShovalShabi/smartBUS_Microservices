package club.smartbus.etc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class responsible for setting up the password encoding mechanism in the application.
 * <p>
 * This configuration defines a bean for the {@link PasswordEncoder}, which is used to securely hash passwords
 * using the BCrypt hashing algorithm.
 * </p>
 */
@Configuration
public class PasswordConfig {

    /**
     * Bean definition for the {@link PasswordEncoder} using the {@link BCryptPasswordEncoder} algorithm.
     * <p>
     * The {@link BCryptPasswordEncoder} is a widely used algorithm for password hashing because of its
     * strength and security features, such as salting and adaptive cost, which makes brute-force attacks
     * more difficult.
     * </p>
     *
     * @return a {@link BCryptPasswordEncoder} instance that is used to encode passwords throughout the application.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
