package com.terence.santorini.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.terence.santorini.game.JsonGameBoard;
import com.terence.santorini.game.JsonGameSquare;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SantoriniGameBoardSerializerTest {
  private SantoriniGameboardMapper santoriniGameboardMapper;

  @BeforeEach
  void setUp() {
    santoriniGameboardMapper = new SantoriniGameboardMapper();
  }

  @Nested
  class JsonGameBoardToGameboard {
    @Test
    void mapEmptyGameboard() throws GameBoardException {
      SantoriniGameBoard gameBoard = SantoriniGameBoard.initiateBoard();

      JsonGameBoard jsonGameBoard =
          santoriniGameboardMapper.gameboardToJsonRepresentation(gameBoard);

      List<List<JsonGameSquare>> gameboard = jsonGameBoard.getGameboard();

      gameboard.forEach(
          row -> {
            row.forEach(
                square -> {
                  assertEquals(0, square.getLevels());
                  assertNull(square.getWorkerId());
                });
          });
    }

    @Test
    void mapPopulatedGameboard() throws GameBoardException {
      SantoriniGameBoard gameBoard = SantoriniGameBoard.initiateBoard();

      gameBoard.placeWorker(GridPosition.A1, SantoriniPlayer.A);

      gameBoard.placeBlock(GridPosition.A2, "A1");
      gameBoard.moveWorker(GridPosition.A2, "A1");

      gameBoard.placeWorker(GridPosition.C3, SantoriniPlayer.B);

      gameBoard.placeBlock(GridPosition.D3, "B1");
      gameBoard.placeBlock(GridPosition.D3, "B1");
      gameBoard.moveWorker(GridPosition.B4, "B1");

      JsonGameBoard jsonGameBoard =
          santoriniGameboardMapper.gameboardToJsonRepresentation(gameBoard);

      List<List<JsonGameSquare>> gameboard = jsonGameBoard.getGameboard();

      JsonGameSquare a2Square =
          gameboard.get(GridPosition.A2.getRowIndex()).get(GridPosition.A2.getColumnIndex());
      assertEquals(1, a2Square.getLevels());
      assertEquals("A1", a2Square.getWorkerId());

      JsonGameSquare d3Square =
          gameboard.get(GridPosition.D3.getRowIndex()).get(GridPosition.D3.getColumnIndex());
      assertEquals(2, d3Square.getLevels());
      assertNull(d3Square.getWorkerId());

      JsonGameSquare b4Square =
          gameboard.get(GridPosition.B4.getRowIndex()).get(GridPosition.B4.getColumnIndex());
      assertEquals(0, b4Square.getLevels());
      assertEquals("B1", b4Square.getWorkerId());
    }
  }

  @Nested
  class GameboardToJsonRepresentation {
    @Test
    void mapEmptyJsonGameboard() {
      List<List<JsonGameSquare>> jsonSquares = new ArrayList<>(5);
      jsonSquares.add(
          0,
          Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(
          1,
          Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(
          2,
          Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(
          3,
          Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(
          4,
          Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));

      JsonGameBoard jsonGameBoard = new JsonGameBoard();
      jsonGameBoard.setGameboard(jsonSquares);

      SantoriniGameBoard santoriniGameBoard =
          santoriniGameboardMapper.jsonRepresentationToGameboard(jsonGameBoard);

      santoriniGameBoard.gameBoard.forEach(
          row -> {
            row.forEach(
                square -> {
                  assertEquals(0, square.getLevels());
                  assertFalse(square.getWorker().isPresent());
                });
          });

      HashMap<String, GridPosition> playerIdGridPositionLookup =
          santoriniGameBoard.playerIdGridPositionLookup;
      assertTrue(playerIdGridPositionLookup.entrySet().isEmpty());
    }

    @Test
    void mapPopulatedJsonGameboard() {
      List<List<JsonGameSquare>> jsonSquares = new ArrayList<>(5);
      jsonSquares.add(
          0,
          Arrays.asList(
              new JsonGameSquare(1, "A1"),
              emptySquare(),
              emptySquare(),
              emptySquare(),
              emptySquare()));
      jsonSquares.add(
          1,
          Arrays.asList(
              emptySquare(),
              new JsonGameSquare(2, null),
              emptySquare(),
              emptySquare(),
              emptySquare()));
      jsonSquares.add(
          2,
          Arrays.asList(
              emptySquare(),
              emptySquare(),
              new JsonGameSquare(4, null),
              emptySquare(),
              emptySquare()));
      jsonSquares.add(
          3,
          Arrays.asList(
              emptySquare(),
              emptySquare(),
              emptySquare(),
              new JsonGameSquare(3, "B1"),
              emptySquare()));
      jsonSquares.add(
          4,
          Arrays.asList(
              emptySquare(),
              emptySquare(),
              emptySquare(),
              emptySquare(),
              new JsonGameSquare(0, "B2")));

      JsonGameBoard jsonGameBoard = new JsonGameBoard();
      jsonGameBoard.setGameboard(jsonSquares);

      SantoriniGameBoard santoriniGameBoard =
          santoriniGameboardMapper.jsonRepresentationToGameboard(jsonGameBoard);

      List<List<SantoriniGameSquare>> gameBoard = santoriniGameBoard.gameBoard;

      SantoriniGameSquare a1Square =
          gameBoard.get(GridPosition.A1.getRowIndex()).get(GridPosition.A1.getColumnIndex());
      assertEquals(1, a1Square.getLevels());
      assertTrue(a1Square.getWorker().isPresent());
      assertEquals("A1", a1Square.getWorker().get());

      SantoriniGameSquare b2Square =
          gameBoard.get(GridPosition.B2.getRowIndex()).get(GridPosition.B2.getColumnIndex());
      assertEquals(2, b2Square.getLevels());
      assertFalse(b2Square.getWorker().isPresent());

      SantoriniGameSquare c3Square =
          gameBoard.get(GridPosition.C3.getRowIndex()).get(GridPosition.C3.getColumnIndex());
      assertEquals(4, c3Square.getLevels());
      assertFalse(c3Square.getWorker().isPresent());

      SantoriniGameSquare d4Square =
          gameBoard.get(GridPosition.D4.getRowIndex()).get(GridPosition.D4.getColumnIndex());
      assertEquals(3, d4Square.getLevels());
      assertTrue(d4Square.getWorker().isPresent());
      assertEquals("B1", d4Square.getWorker().get());

      SantoriniGameSquare e5Square =
          gameBoard.get(GridPosition.E5.getRowIndex()).get(GridPosition.E5.getColumnIndex());
      assertEquals(0, e5Square.getLevels());
      assertTrue(e5Square.getWorker().isPresent());
      assertEquals("B2", e5Square.getWorker().get());

      HashMap<String, GridPosition> playerIdGridPositionLookup =
          santoriniGameBoard.playerIdGridPositionLookup;
      assertEquals(GridPosition.A1, playerIdGridPositionLookup.get("A1"));
      assertEquals(GridPosition.D4, playerIdGridPositionLookup.get("B1"));
      assertEquals(GridPosition.E5, playerIdGridPositionLookup.get("B2"));
    }

    private JsonGameSquare emptySquare() {
      return new JsonGameSquare(0, null);
    }
  }
}
