package com.terence.santorini.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terence.santorini.gamelogic.GameBoardException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class GameBoardSerializerIntTest {

  @Autowired
  GameSerializer gameSerializer;

  @Autowired
  ObjectMapper objectMapper;

  @Nested
  class Serialize {
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

      String jsonString = gameSerializer.beanToJson(jsonGameRepresentation);

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

      String jsonString = gameSerializer.beanToJson(jsonGameRepresentation);

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
  class Deserialize {
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

      JsonGameRepresentation jsonGameRepresentation = gameSerializer.jsonToBean(input);

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

      JsonGameRepresentation jsonGameRepresentation = gameSerializer.jsonToBean(input);

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
}
