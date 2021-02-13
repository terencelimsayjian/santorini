package com.terence.santorini.gamelogic;

import com.terence.santorini.game.JsonGameBoard;
import com.terence.santorini.game.JsonGameSquare;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SantoriniGameboardMapper {
  public SantoriniGameboardMapper() {
  }

  JsonGameBoard gameboardToJsonRepresentation(SantoriniGameBoard santoriniGameBoard) {
    List<List<SantoriniGameSquare>> gameBoard = santoriniGameBoard.gameBoard;

    List<List<JsonGameSquare>> jsonBoard = new ArrayList<>(5);

    gameBoard.forEach(row -> {
      List<JsonGameSquare> jsonRow = row.stream()
          .map(this::toJsonSquareRepresentation)
          .collect(Collectors.toList());

      jsonBoard.add(jsonRow);

    });

    JsonGameBoard jsonGameBoard = new JsonGameBoard();
    jsonGameBoard.setGameboard(jsonBoard);

    return jsonGameBoard;
  }

  private JsonGameSquare toJsonSquareRepresentation(SantoriniGameSquare santoriniGameSquare) {
    String workerId = santoriniGameSquare.getWorker().map(SantoriniWorker::getId).orElse(null);

    return new JsonGameSquare(santoriniGameSquare.getLevels(), workerId);
  }

  SantoriniGameBoard jsonRepresentationToGameboard(JsonGameBoard jsonGameBoard) {
    List<List<SantoriniGameSquare>> gameboard = new ArrayList<>(5);
    HashMap<String, GridPosition> playerIdGridPositionLookup = new HashMap<>();

    SantoriniGameBoard santoriniGameBoard = SantoriniGameBoard.initiateBoard();
    List<List<JsonGameSquare>> jsonGameboardRepresentation = jsonGameBoard.getGameboard();

    for (int i = 0; i < jsonGameboardRepresentation.size(); i++) {
      List<JsonGameSquare> jsonRowRepresentation = jsonGameboardRepresentation.get(i);
      List<SantoriniGameSquare> santoriniGameSquares = new ArrayList<>(5);

      for (int j = 0; j < jsonRowRepresentation.size(); j++) {
        JsonGameSquare jsonGameSquare = jsonRowRepresentation.get(j);
        santoriniGameSquares.add(toSantoriniSquare(jsonGameSquare));

        if (jsonGameSquare.getWorkerId() != null) {
          playerIdGridPositionLookup.put(jsonGameSquare.getWorkerId(), GridPosition.from(i, j).orElseThrow());
        }
      }

      gameboard.add(santoriniGameSquares);
    }

    santoriniGameBoard.gameBoard = gameboard;
    santoriniGameBoard.playerIdGridPositionLookup = playerIdGridPositionLookup;

    return santoriniGameBoard;
  }

  private SantoriniGameSquare toSantoriniSquare(JsonGameSquare jsonSquare) {
    return SantoriniGameSquare.fromExistingSquare(jsonSquare.getWorkerId(), jsonSquare.getLevels());
  }

}
