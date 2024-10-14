package club.smartbus.service.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * RedisTemplate for general data (PassengerWSMessage, DriverWSMessage, etc.).
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplateForData(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Use StringRedisSerializer for keys (e.g., session IDs)
        template.setKeySerializer(new StringRedisSerializer());

        // Use Jackson2JsonRedisSerializer for values (e.g., PassengerWSMessage, DriverWSMessage)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
