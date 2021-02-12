package com.terence.santorini.gamelogic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonGameRepresentationToGameBoardMapper {

  public JsonGameRepresentationToGameBoardMapper() {
  }

  public JsonGameRepresentation gameboardToJsonRepresentation(SantoriniBoard santoriniBoard) {
    List<List<SantoriniSquare>> gameBoard = santoriniBoard.gameBoard;

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

  private JsonSquareRepresentation toJsonSquareRepresentation(SantoriniSquare santoriniSquare) {
    String workerId = santoriniSquare.getWorker().map(SantoriniWorker::getId).orElse(null);

    return new JsonSquareRepresentation(santoriniSquare.getLevels(), workerId);
  }

  public SantoriniBoard jsonRepresntationToGameboard(JsonGameRepresentation jsonGameRepresentation) {

    SantoriniBoard santoriniBoard = SantoriniBoard.initiateBoard();

    List<List<SantoriniSquare>> gameboard = new ArrayList<>(5);

    jsonGameRepresentation.getGameboard().forEach(row -> {
      List<SantoriniSquare> rowOfSquares = row.stream()
          .map(this::toSantoriniSquare)
          .collect(Collectors.toList());

      gameboard.add(rowOfSquares);

    });

    santoriniBoard.gameBoard = gameboard;

    return santoriniBoard;
  }

  private SantoriniSquare toSantoriniSquare(JsonSquareRepresentation jsonSquare) {
    return SantoriniSquare.fromExistingSquare(jsonSquare.getWorkerId(), jsonSquare.getLevels());
  }

}
