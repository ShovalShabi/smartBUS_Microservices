package club.smartbus.service.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuration class for setting up Redis templates and ObjectMapper.
 * This class provides the configuration needed for using Redis as a data store,
 * specifically for handling WebSocket messages such as PassengerWSMessage and DriverWSMessage.
 */
@Configuration
public class RedisConfig {

    /**
     * Creates and configures a RedisTemplate for storing general data in Redis (e.g., WebSocket messages).
     * The template uses a {@link StringRedisSerializer} for keys and a {@link GenericJackson2JsonRedisSerializer} for values.
     *
     * <p>This configuration ensures that session data (e.g., WebSocket messages for passengers and drivers) is stored
     * in Redis with string keys and JSON-serialized values.</p>
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
}
