package org.example.languagemaster.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisCacheService {
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private ObjectMapper objectMapper;

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

  public <T> void addToSet(String tableName, String key, T data, long timeout, TimeUnit unit) {
    try {
      String fullKey = tableName + "_" + key;
      String jsonData = objectMapper.writeValueAsString(data);
      redisTemplate.opsForSet().add(fullKey, jsonData);
      redisTemplate.expire(fullKey, timeout, unit);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize object for Redis", e);
    }
  }
  public <T> void addToList(String tableName, String key, T data, long timeout, TimeUnit unit) {
    try {
      String fullKey = tableName + "_" + key;
      String jsonData = objectMapper.writeValueAsString(data);
      redisTemplate.opsForList().rightPush(fullKey, jsonData);
      redisTemplate.expire(fullKey, timeout, unit);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize object for Redis", e);
    }
  }

  public <T> List<T> getList(String tableName, String key, TypeReference<T> typeReference) {
    String fullKey = tableName + "_" + key;
    List<String> json =  redisTemplate.opsForList().range(fullKey, 0, -1);
    if (json.isEmpty()) return Collections.emptyList();

    return json.stream()
            .map(
                    v1 -> {
                      try {
                        return objectMapper.readValue(v1, typeReference);
                      } catch (IOException e) {
                        throw new RuntimeException("Failed to deserialize object from Redis Set", e);
                      }
                    })
            .collect(Collectors.toList());
  }

  public <T> Set<T> getSet(String tableName, String key, TypeReference<T> typeReference) {
    String fullKey = tableName + "_" + key;
    Set<String> json = redisTemplate.opsForSet().members(fullKey);
    if (json.isEmpty()) return Collections.emptySet();

      return json.stream()
        .map(
            v1 -> {
              try {
                return objectMapper.readValue(v1, typeReference);
              } catch (IOException e) {
                throw new RuntimeException("Failed to deserialize object from Redis Set", e);
              }
            })
        .collect(Collectors.toSet());
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
