package org.example.languagemaster.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisCacheService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public RedisCacheService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public <T> void set(String tableName, String key, T data, long timeout, TimeUnit unit) {
        try {
            String fullKey = tableName + "_" + key;
            String jsonData = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(fullKey, jsonData, timeout, unit);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object for Redis", e);
        }
    }

    public <T> T get(String tableName, String key, TypeReference<T> typeReference) {
        try {
            String fullKey = tableName + "_" + key;
            String json = redisTemplate.opsForValue().get(fullKey);
            if (json == null) return null;
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize object from Redis", e);
        }
    }

    public void delete(String tableName, String key) {
        redisTemplate.delete(tableName + "_" + key);
    }
}
