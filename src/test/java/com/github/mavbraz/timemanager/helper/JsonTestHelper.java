package com.github.mavbraz.timemanager.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonTestHelper {

  private static final ObjectMapper mapper =
      new ObjectMapper().registerModule(new JavaTimeModule());

  public static <T> String asJsonString(T obj) throws JsonProcessingException {
    return mapper.writeValueAsString(obj);
  }

  public static <T> T fromJsonString(String json, Class<T> objClass)
      throws JsonProcessingException {
    return mapper.readValue(json, objClass);
  }

  public static <T> T fromJsonString(String json, TypeReference<T> objTypeReference)
      throws JsonProcessingException {
    return mapper.readValue(json, objTypeReference);
  }
}
