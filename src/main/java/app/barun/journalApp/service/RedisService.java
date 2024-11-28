package app.barun.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper(); // Reuse for efficiency

    /**
     * Retrieve a value from Redis and deserialize it into the specified class.
     *
     * @param key         the Redis key
     * @param entityClass the class to deserialize into
     * @param <T>         the type of the returned object
     * @return the deserialized object, or null if the key does not exist or deserialization fails
     */
    public <T> T get(String key, Class<T> entityClass) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                log.warn("No value found in Redis for key: {}", key);
                return null;
            }
            return objectMapper.readValue(value, entityClass);
        } catch (Exception e) {
            log.error("Exception while fetching key '{}' from Redis: {}", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Store an object in Redis as a JSON string with a time-to-live.
     *
     * @param key the Redis key
     * @param object the object to store
     * @param ttl the time-to-live in seconds
     */
    public void set(String key, Object object, Long ttl) {
        try {
            String jsonValue = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Exception while setting key '{}' in Redis: {}", key, e.getMessage(), e);
        }
    }
}
