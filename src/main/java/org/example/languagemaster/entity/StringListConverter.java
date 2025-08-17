package org.example.languagemaster.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<String> stringList) {
    if (stringList == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(stringList);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting List to JSON String", e);
    }
  }

  @Override
  public List<String> convertToEntityAttribute(String jsonString) {
    if (jsonString == null) {
      return Collections.emptyList();
    }
    try {
      return objectMapper.readValue(
          jsonString,
          objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
    } catch (IOException e) {
      throw new IllegalArgumentException("Error converting JSON String to List", e);
    }
  }
}
