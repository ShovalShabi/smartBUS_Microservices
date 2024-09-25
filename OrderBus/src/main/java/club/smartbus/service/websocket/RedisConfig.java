package club.smartbus.service.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.socket.WebSocketSession;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, WebSocketSession> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, WebSocketSession> template = new RedisTemplate<>();

        // Use StringRedisSerializer for keys (session IDs)
        template.setKeySerializer(new StringRedisSerializer());

        // Use Jackson2JsonRedisSerializer for values (WebSocketSession objects)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
