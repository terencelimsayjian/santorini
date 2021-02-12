package com.terence.santorini.gamelogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terence.santorini.gamelogic.JsonGameRepresentation;
import org.springframework.stereotype.Component;

@Component
public class GameSerializer {
  private ObjectMapper objectMapper;

  public GameSerializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public String beanToJson(JsonGameRepresentation jsonGameRepresentation) throws JsonProcessingException {
    return objectMapper.writeValueAsString(jsonGameRepresentation);
  }

  public JsonGameRepresentation jsonToBean(String jsonString) throws JsonProcessingException {
    return objectMapper.readValue(jsonString, JsonGameRepresentation.class);
  }
}