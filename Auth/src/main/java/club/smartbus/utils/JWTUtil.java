package club.smartbus.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for handling JWT (JSON Web Token) operations such as token generation, validation, and renewal.
 * <p>
 * The class uses HMAC-SHA256 to sign the tokens and stores tokens in memory using a {@link ConcurrentHashMap}.
 * In a production environment, this token store should be replaced with a more durable storage such as a database
 * or Redis.
 * </p>
 */
@Component
@Slf4j
public class JWTUtil {

    // Store a strong, secure key (use a more complex key in production)
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Simulating a token store (this should be an actual persistence layer like Redis, DB)
    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    /**
     * Generates a JWT token for a given email.
     * <p>
     * The token is valid for 10 hours and is stored in an in-memory token store.
     * </p>
     *
     * @param email The subject (email) for which the token is generated.
     * @return A JWT token.
     */
    public String generateToken(String email) {
        log.info("Generating new JWT token for user: {}", email);
        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // Token valid for 10 hours
                .signWith(SECRET_KEY)
                .compact();

        // Store token in memory (or in your persistent storage)
        tokenStore.put(email, token);
        return token;
    }

    /**
     * Retrieves the existing token for a user if available.
     *
     * @param email The user's email.
     * @return The JWT token or null if not found.
     */
    public String getTokenForUser(String email) {
        return tokenStore.get(email);
    }

    /**
     * Validates the existing token.
     *
     * @param token The JWT token to be validated.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Renews an existing valid JWT token by issuing a new one with the same subject.
     * <p>
     * The new token is valid for 14 days.
     * </p>
     *
     * @param existingToken The current JWT token.
     * @return A renewed JWT token.
     */
    public String renewToken(String existingToken) {
        String email = extractEmail(existingToken);
        log.info("Renewing JWT token for user: {}", email);

        // Generate a new token with a fresh expiration time
        String newToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 14))  // Token valid for 14 days
                .signWith(SECRET_KEY)
                .compact();

        // Update token store
        tokenStore.put(email, newToken);
        return newToken;
    }

    /**
     * Extracts the email (subject) from the token.
     *
     * @param token The JWT token.
     * @return The email (subject) extracted from the token.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validates the token with the provided email.
     *
     * @param token The JWT token.
     * @param email The email to validate.
     * @return True if the token is valid for the given email, false otherwise.
     */
    public boolean validateToken(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    /**
     * Extracts all claims from the token.
     *
     * @param token The JWT token.
     * @return The claims extracted from the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts a specific claim from the token using a claims' resolver.
     *
     * @param token The JWT token.
     * @param claimsResolver A function to resolve the claim.
     * @param <T> The type of the claim.
     * @return The extracted claim.
     */
    private <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Checks if the token is expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    /**
     * Extracts the expiration date from the token.
     *
     * @param token The JWT token.
     * @return The expiration date.
     */
    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
}
