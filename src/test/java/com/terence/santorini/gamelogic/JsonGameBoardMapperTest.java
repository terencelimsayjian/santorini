package com.terence.santorini.gamelogic;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonGameBoardMapperTest {
  JsonGameBoardMapper mapper = new JsonGameBoardMapper();

  @Nested
  class JsonGameRepresentationToGameboard {
    @Test
    void mapEmptyGameboard() throws GameBoardException {
      SantoriniBoard gameBoard = SantoriniBoard.initiateBoard();

      JsonGameRepresentation jsonGameRepresentation = mapper.gameboardToJsonRepresentation(gameBoard);

      List<List<JsonSquareRepresentation>> gameboard = jsonGameRepresentation.getGameboard();

      gameboard.forEach(row -> {
        row.forEach(square -> {
          assertEquals(0, square.getLevels());
          assertNull(square.getWorkerId());
        });
      });
    }

    @Test
    void mapPopulatedGameboard() throws GameBoardException {
      SantoriniBoard gameBoard = SantoriniBoard.initiateBoard();

      SantoriniWorker a1Worker = new SantoriniWorker("A1");
      gameBoard.placeWorker(GridPosition.A1, a1Worker);

      gameBoard.placeBlock(GridPosition.A2, a1Worker);
      gameBoard.moveWorker(GridPosition.A2, a1Worker);

      SantoriniWorker b1Worker = new SantoriniWorker("B1");
      gameBoard.placeWorker(GridPosition.C3, b1Worker);

      gameBoard.placeBlock(GridPosition.D3, b1Worker);
      gameBoard.placeBlock(GridPosition.D3, b1Worker);
      gameBoard.moveWorker(GridPosition.B4, b1Worker);

      JsonGameRepresentation jsonGameRepresentation = mapper.gameboardToJsonRepresentation(gameBoard);

      List<List<JsonSquareRepresentation>> gameboard = jsonGameRepresentation.getGameboard();

      JsonSquareRepresentation a2Square = gameboard.get(GridPosition.A2.getRowIndex()).get(GridPosition.A2.getColumnIndex());
      assertEquals(1, a2Square.getLevels());
      assertEquals("A1", a2Square.getWorkerId());

      JsonSquareRepresentation d3Square = gameboard.get(GridPosition.D3.getRowIndex()).get(GridPosition.D3.getColumnIndex());
      assertEquals(2, d3Square.getLevels());
      assertNull(d3Square.getWorkerId());

      JsonSquareRepresentation b4Square = gameboard.get(GridPosition.B4.getRowIndex()).get(GridPosition.B4.getColumnIndex());
      assertEquals(0, b4Square.getLevels());
      assertEquals("B1", b4Square.getWorkerId());
    }
  }

  @Nested
  class GameboardToJsonRepresentation {

    @Test
    void mapEmptyJsonGameboard() {
      List<List<JsonSquareRepresentation>> jsonSquares = new ArrayList<>(5);
      jsonSquares.add(0, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(1, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(2, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(3, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(4, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));

      JsonGameRepresentation jsonGameRepresentation = new JsonGameRepresentation();
      jsonGameRepresentation.setGameboard(jsonSquares);

      SantoriniBoard santoriniBoard = mapper.jsonRepresentationToGameboard(jsonGameRepresentation);

      santoriniBoard.gameBoard.forEach(row -> {
        row.forEach(square -> {
          assertEquals(0, square.getLevels());
          assertFalse(square.getWorker().isPresent());
        });
      });
    }

    @Test
    void mapPopulatedJsonGameboard() {
      List<List<JsonSquareRepresentation>> jsonSquares = new ArrayList<>(5);
      jsonSquares.add(0, Arrays.asList(new JsonSquareRepresentation(1, "A1"), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(1, Arrays.asList(emptySquare(), new JsonSquareRepresentation(2, null), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(2, Arrays.asList(emptySquare(), emptySquare(), new JsonSquareRepresentation(4, null), emptySquare(), emptySquare()));
      jsonSquares.add(3, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), new JsonSquareRepresentation(3, "B1"), emptySquare()));
      jsonSquares.add(4, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), new JsonSquareRepresentation(0, "B2")));

      JsonGameRepresentation jsonGameRepresentation = new JsonGameRepresentation();
      jsonGameRepresentation.setGameboard(jsonSquares);

      SantoriniBoard santoriniBoard = mapper.jsonRepresentationToGameboard(jsonGameRepresentation);

      List<List<SantoriniSquare>> gameBoard = santoriniBoard.gameBoard;

      SantoriniSquare a1Square = gameBoard.get(GridPosition.A1.getRowIndex()).get(GridPosition.A1.getColumnIndex());
      assertEquals(1, a1Square.getLevels());
      assertTrue(a1Square.getWorker().isPresent());
      assertEquals("A1", a1Square.getWorker().get().getId());

      SantoriniSquare b2Square = gameBoard.get(GridPosition.B2.getRowIndex()).get(GridPosition.B2.getColumnIndex());
      assertEquals(2, b2Square.getLevels());
      assertFalse(b2Square.getWorker().isPresent());

      SantoriniSquare c3Square = gameBoard.get(GridPosition.C3.getRowIndex()).get(GridPosition.C3.getColumnIndex());
      assertEquals(4, c3Square.getLevels());
      assertFalse(c3Square.getWorker().isPresent());

      SantoriniSquare d4Square = gameBoard.get(GridPosition.D4.getRowIndex()).get(GridPosition.D4.getColumnIndex());
      assertEquals(3, d4Square.getLevels());
      assertTrue(d4Square.getWorker().isPresent());
      assertEquals("B1", d4Square.getWorker().get().getId());

      SantoriniSquare e5Square = gameBoard.get(GridPosition.E5.getRowIndex()).get(GridPosition.E5.getColumnIndex());
      assertEquals(0, e5Square.getLevels());
      assertTrue(e5Square.getWorker().isPresent());
      assertEquals("B2", e5Square.getWorker().get().getId());
    }


    private JsonSquareRepresentation emptySquare() {
      return new JsonSquareRepresentation(0, null);
    }
  }


}
