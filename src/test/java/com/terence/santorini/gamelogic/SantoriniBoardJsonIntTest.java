package com.terence.santorini.gamelogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SantoriniGameBoardSerializerIntegrationTest {
  private SantoriniBoardSerializer santoriniBoardSerializer;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    santoriniBoardSerializer = new SantoriniBoardSerializer(objectMapper);
  }

  @Nested
  class SerializeJsonRepresentationToString {
    @Test
    void gameBoardToString() throws JsonProcessingException {
      List<List<JsonSquareRepresentation>> jsonSquares = new ArrayList<>(5);
      jsonSquares.add(0, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(1, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(2, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(3, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(4, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));

      JsonGameRepresentation jsonGameRepresentation = new JsonGameRepresentation();
      jsonGameRepresentation.setGameboard(jsonSquares);

      String jsonString = santoriniBoardSerializer.pojoToJson(jsonGameRepresentation);

      JsonNode jsonNode = objectMapper.readTree(jsonString);
      JsonNode gameboard = jsonNode.get("gameboard");

      for (int i = 0; i < 5; i++) {
        JsonNode jsonGameboardRow = gameboard.get(i);

        assertEquals(5, jsonGameboardRow.size());

        for (int j = 0; j < 5; j++) {
          JsonNode jsonSantoriniSquare = jsonGameboardRow.get(j);

          assertTrue(jsonSantoriniSquare.has("workerId"));
          assertTrue(jsonSantoriniSquare.has("levels"));

          assertEquals(0, jsonSantoriniSquare.get("levels").asInt());
        }
      }
    }

    @Test
    void gameBoardToStringWithHigherLevelAndWorker() throws JsonProcessingException {
      List<List<JsonSquareRepresentation>> jsonSquares = new ArrayList<>(5);
      jsonSquares.add(0, Arrays.asList(new JsonSquareRepresentation(1, "A1"), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(1, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(2, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(3, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
      jsonSquares.add(4, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));

      JsonGameRepresentation jsonGameRepresentation = new JsonGameRepresentation();
      jsonGameRepresentation.setGameboard(jsonSquares);

      String jsonString = santoriniBoardSerializer.pojoToJson(jsonGameRepresentation);

      JsonNode jsonNode = objectMapper.readTree(jsonString);
      JsonNode gameboard = jsonNode.get("gameboard");

      JsonNode jsonGameboardRow = gameboard.get(0);
      JsonNode jsonSantoriniSquare = jsonGameboardRow.get(0);
      assertTrue(jsonSantoriniSquare.has("workerId"));
      assertTrue(jsonSantoriniSquare.has("levels"));

      assertEquals("A1", jsonSantoriniSquare.get("workerId").asText());
      assertEquals(1, jsonSantoriniSquare.get("levels").asInt());
    }

    private JsonSquareRepresentation emptySquare() {
      return new JsonSquareRepresentation(0, null);
    }
  }

  @Nested
  class DeserializeStringToJsonRepresentation {
    @Test
    void jsonStringToEmptyGameBoard() throws GameBoardException, JsonProcessingException {
      String input = "{\n" +
                     "    \"gameboard\": [\n" +
                     "        [{ \"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}],\n" +
                     "        [{ \"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}],\n" +
                     "        [{ \"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}],\n" +
                     "        [{ \"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}],\n" +
                     "        [{ \"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}]\n" +
                     "    ]\n" +
                     "}";

      JsonGameRepresentation jsonGameRepresentation = santoriniBoardSerializer.jsonToPojo(input);

      List<List<JsonSquareRepresentation>> gameboard = jsonGameRepresentation.getGameboard();

      gameboard.forEach(gb -> {
        gb.forEach(gs -> {
          assertEquals(0, gs.getLevels());
          assertNull(gs.getWorkerId());
        });
      });
    }

    @Test
    void jsonStringToPopulatedGameBoard() throws GameBoardException, JsonProcessingException {
      String input = "{\n" +
                     "    \"gameboard\": [\n" +
                     "        [{ \"levels\": 1}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}],\n" +
                     "        [{ \"levels\": 0}, {\"workerId\": \"A1\", \"levels\": 2}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}],\n" +
                     "        [{ \"levels\": 0}, {\"levels\": 0}, {\"workerId\": \"B1\", \"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}],\n" +
                     "        [{ \"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"workerId\": \"A2\", \"levels\": 3}, {\"levels\": 0}],\n" +
                     "        [{ \"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"levels\": 0}, {\"workerId\": \"B2\", \"levels\": 1}]\n" +
                     "    ]\n" +
                     "}";

      JsonGameRepresentation jsonGameRepresentation = santoriniBoardSerializer.jsonToPojo(input);

      List<List<JsonSquareRepresentation>> gameboard = jsonGameRepresentation.getGameboard();

      JsonSquareRepresentation a1square = gameboard.get(0).get(0);
      assertEquals(1, a1square.getLevels());
      assertNull(a1square.getWorkerId());

      JsonSquareRepresentation b2square = gameboard.get(1).get(1);
      assertEquals(2, b2square.getLevels());
      assertEquals("A1", b2square.getWorkerId());

      JsonSquareRepresentation c3square = gameboard.get(2).get(2);
      assertEquals(0, c3square.getLevels());
      assertEquals("B1", c3square.getWorkerId());

      JsonSquareRepresentation d4square = gameboard.get(3).get(3);
      assertEquals(3, d4square.getLevels());
      assertEquals("A2", d4square.getWorkerId());

      JsonSquareRepresentation e5square = gameboard.get(4).get(4);
      assertEquals(1, e5square.getLevels());
      assertEquals("B2", e5square.getWorkerId());
    }
  }

  @Nested
  class JsonGameRepresentationToGameboard {
    @Test
    void mapEmptyGameboard() throws GameBoardException {
      SantoriniGameBoard gameBoard = SantoriniGameBoard.initiateBoard();

      JsonGameRepresentation jsonGameRepresentation = santoriniBoardSerializer.gameboardToJsonRepresentation(gameBoard);

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
      SantoriniGameBoard gameBoard = SantoriniGameBoard.initiateBoard();

      SantoriniWorker a1Worker = new SantoriniWorker("A1");
      gameBoard.placeWorker(GridPosition.A1, a1Worker);

      gameBoard.placeBlock(GridPosition.A2, a1Worker);
      gameBoard.moveWorker(GridPosition.A2, a1Worker);

      SantoriniWorker b1Worker = new SantoriniWorker("B1");
      gameBoard.placeWorker(GridPosition.C3, b1Worker);

      gameBoard.placeBlock(GridPosition.D3, b1Worker);
      gameBoard.placeBlock(GridPosition.D3, b1Worker);
      gameBoard.moveWorker(GridPosition.B4, b1Worker);

      JsonGameRepresentation jsonGameRepresentation = santoriniBoardSerializer.gameboardToJsonRepresentation(gameBoard);

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

      SantoriniGameBoard santoriniGameBoard = santoriniBoardSerializer.jsonRepresentationToGameboard(jsonGameRepresentation);

      santoriniGameBoard.gameBoard.forEach(row -> {
        row.forEach(square -> {
          assertEquals(0, square.getLevels());
          assertFalse(square.getWorker().isPresent());
        });
      });

      HashMap<String, GridPosition> playerIdGridPositionLookup = santoriniGameBoard.playerIdGridPositionLookup;
      assertTrue(playerIdGridPositionLookup.entrySet().isEmpty());
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

      SantoriniGameBoard santoriniGameBoard = santoriniBoardSerializer.jsonRepresentationToGameboard(jsonGameRepresentation);

      List<List<SantoriniGameSquare>> gameBoard = santoriniGameBoard.gameBoard;

      SantoriniGameSquare a1Square = gameBoard.get(GridPosition.A1.getRowIndex()).get(GridPosition.A1.getColumnIndex());
      assertEquals(1, a1Square.getLevels());
      assertTrue(a1Square.getWorker().isPresent());
      assertEquals("A1", a1Square.getWorker().get().getId());

      SantoriniGameSquare b2Square = gameBoard.get(GridPosition.B2.getRowIndex()).get(GridPosition.B2.getColumnIndex());
      assertEquals(2, b2Square.getLevels());
      assertFalse(b2Square.getWorker().isPresent());

      SantoriniGameSquare c3Square = gameBoard.get(GridPosition.C3.getRowIndex()).get(GridPosition.C3.getColumnIndex());
      assertEquals(4, c3Square.getLevels());
      assertFalse(c3Square.getWorker().isPresent());

      SantoriniGameSquare d4Square = gameBoard.get(GridPosition.D4.getRowIndex()).get(GridPosition.D4.getColumnIndex());
      assertEquals(3, d4Square.getLevels());
      assertTrue(d4Square.getWorker().isPresent());
      assertEquals("B1", d4Square.getWorker().get().getId());

      SantoriniGameSquare e5Square = gameBoard.get(GridPosition.E5.getRowIndex()).get(GridPosition.E5.getColumnIndex());
      assertEquals(0, e5Square.getLevels());
      assertTrue(e5Square.getWorker().isPresent());
      assertEquals("B2", e5Square.getWorker().get().getId());

      HashMap<String, GridPosition> playerIdGridPositionLookup = santoriniGameBoard.playerIdGridPositionLookup;
      assertEquals(GridPosition.A1, playerIdGridPositionLookup.get("A1"));
      assertEquals(GridPosition.D4, playerIdGridPositionLookup.get("B1"));
      assertEquals(GridPosition.E5, playerIdGridPositionLookup.get("B2"));
    }

    private JsonSquareRepresentation emptySquare() {
      return new JsonSquareRepresentation(0, null);
    }
  }
}
