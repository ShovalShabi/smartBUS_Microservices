package club.smartbus.etc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    // Inject the Redis host, port, and password from the application.properties file
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password:}")  // Optional: Default to empty string if not provided
    private String redisPassword;

    /**
     * Creates and configures a RedisTemplate for storing general data in Redis (e.g., WebSocket messages).
     * The template uses a {@link StringRedisSerializer} for keys and a {@link GenericJackson2JsonRedisSerializer} for values.
     *
     * @param connectionFactory The RedisConnectionFactory used to create Redis connections.
     * @return A configured {@link RedisTemplate} instance for storing key-value pairs in Redis.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplateForData(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Use StringRedisSerializer for keys (e.g., session IDs)
        template.setKeySerializer(new StringRedisSerializer());

        // Use GenericJackson2JsonRedisSerializer for values (e.g., PassengerWSMessage, DriverWSMessage)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.setConnectionFactory(connectionFactory);
        return template;
    }

    /**
     * Provides a pre-configured {@link ObjectMapper} bean for JSON serialization/deserialization.
     * This is used to convert Java objects to JSON and vice versa throughout the application.
     *
     * @return A new instance of {@link ObjectMapper}.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * Configures and provides a RedisConnectionFactory using Lettuce with standalone Redis setup.
     * The host, port, and password are injected from the application.properties file.
     *
     * @return A configured LettuceConnectionFactory.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Standalone configuration for Redis
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);  // Injected Redis host
        redisStandaloneConfiguration.setPort(redisPort);  // Injected Redis port

        if (!redisPassword.isEmpty()) {
            redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));  // Injected Redis password if provided
        }

        // Client configuration (adjust timeouts, etc.)
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(60))
                .build();

        // Create the LettuceConnectionFactory using the configurations
        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
    }
}
