package com.terence.santorini.gamelogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SantoriniBoardSerializer {
  private ObjectMapper objectMapper;

  public SantoriniBoardSerializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public SantoriniGameBoard fromJsonString(String jsonString) throws JsonProcessingException {
    JsonGameRepresentation jsonGameRepresentation = jsonToPojo(jsonString);
    return jsonRepresentationToGameboard(jsonGameRepresentation);
  }

  public String toJsonString(SantoriniGameBoard santoriniGameBoard) throws JsonProcessingException {
    JsonGameRepresentation jsonGameRepresentation = gameboardToJsonRepresentation(santoriniGameBoard);
    return pojoToJson(jsonGameRepresentation);
  }

  String pojoToJson(JsonGameRepresentation jsonGameRepresentation) throws JsonProcessingException {
    return objectMapper.writeValueAsString(jsonGameRepresentation);
  }

  JsonGameRepresentation jsonToPojo(String jsonString) throws JsonProcessingException {
    return objectMapper.readValue(jsonString, JsonGameRepresentation.class);
  }

  JsonGameRepresentation gameboardToJsonRepresentation(SantoriniGameBoard santoriniGameBoard) {
    List<List<SantoriniGameSquare>> gameBoard = santoriniGameBoard.gameBoard;

    List<List<JsonSquareRepresentation>> jsonBoard = new ArrayList<>(5);

    gameBoard.forEach(row -> {
      List<JsonSquareRepresentation> jsonRow = row.stream()
          .map(this::toJsonSquareRepresentation)
          .collect(Collectors.toList());

      jsonBoard.add(jsonRow);

    });

    JsonGameRepresentation jsonGameRepresentation = new JsonGameRepresentation();
    jsonGameRepresentation.setGameboard(jsonBoard);

    return jsonGameRepresentation;
  }

  private JsonSquareRepresentation toJsonSquareRepresentation(SantoriniGameSquare santoriniGameSquare) {
    String workerId = santoriniGameSquare.getWorker().map(SantoriniWorker::getId).orElse(null);

    return new JsonSquareRepresentation(santoriniGameSquare.getLevels(), workerId);
  }

  SantoriniGameBoard jsonRepresentationToGameboard(JsonGameRepresentation jsonGameRepresentation) {
    List<List<SantoriniGameSquare>> gameboard = new ArrayList<>(5);
    HashMap<String, GridPosition> playerIdGridPositionLookup = new HashMap<>();

    SantoriniGameBoard santoriniGameBoard = SantoriniGameBoard.initiateBoard();
    List<List<JsonSquareRepresentation>> jsonGameboardRepresentation = jsonGameRepresentation.getGameboard();

    for (int i = 0; i < jsonGameboardRepresentation.size(); i++) {
      List<JsonSquareRepresentation> jsonRowRepresentation = jsonGameboardRepresentation.get(i);
      List<SantoriniGameSquare> santoriniGameSquares = new ArrayList<>(5);

      for (int j = 0; j < jsonRowRepresentation.size(); j++) {
        JsonSquareRepresentation jsonSquareRepresentation = jsonRowRepresentation.get(j);
        santoriniGameSquares.add(toSantoriniSquare(jsonSquareRepresentation));

        if (jsonSquareRepresentation.getWorkerId() != null) {
          playerIdGridPositionLookup.put(jsonSquareRepresentation.getWorkerId(), GridPosition.from(i, j).orElseThrow());
        }
      }

      gameboard.add(santoriniGameSquares);
    }

    santoriniGameBoard.gameBoard = gameboard;
    santoriniGameBoard.playerIdGridPositionLookup = playerIdGridPositionLookup;

    return santoriniGameBoard;
  }

  private SantoriniGameSquare toSantoriniSquare(JsonSquareRepresentation jsonSquare) {
    return SantoriniGameSquare.fromExistingSquare(jsonSquare.getWorkerId(), jsonSquare.getLevels());
  }

}
