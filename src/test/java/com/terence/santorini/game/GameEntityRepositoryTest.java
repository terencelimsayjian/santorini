package com.terence.santorini.game;

import com.terence.santorini.gamelogic.JsonGameRepresentation;
import com.terence.santorini.gamelogic.JsonSquareRepresentation;
import com.terence.santorini.testutil.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameEntityRepositoryTest extends BaseIntegrationTest {

  @Autowired
  private GameRepository gameRepository;

  @Test
  void testSimpleSaveAndFind() {
    List<List<JsonSquareRepresentation>> jsonSquares = new ArrayList<>(5);
    jsonSquares.add(0, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
    jsonSquares.add(1, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
    jsonSquares.add(2, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
    jsonSquares.add(3, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
    jsonSquares.add(4, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));

    JsonGameRepresentation jsonGameRepresentation = new JsonGameRepresentation();
    jsonGameRepresentation.setGameboard(jsonSquares);

    GameEntity gameEntity = new GameEntity();
    gameEntity.setGameBoard(jsonGameRepresentation);
    gameEntity.setPlayer1("Player1");
    gameEntity.setPlayer2("Player2");
    gameEntity.setWinner("Winner");
    Instant createdAt = Instant.now();
    gameEntity.setCreatedAt(createdAt);
    Instant updatedAt = Instant.now().plus(1000, ChronoUnit.SECONDS);
    gameEntity.setUpdatedAt(updatedAt);

    GameEntity savedGameEntity = gameRepository.save(gameEntity);

    Optional<GameEntity> optionalGame = gameRepository.findById(savedGameEntity.getId());

    assertTrue(optionalGame.isPresent());
    GameEntity actual = optionalGame.get();

    assertEquals("Player1", actual.getPlayer1());
    assertEquals("Player2", actual.getPlayer2());
    assertEquals("Winner", actual.getWinner());
    assertEquals(createdAt, actual.getCreatedAt());
    assertEquals(updatedAt, actual.getUpdatedAt());

    JsonGameRepresentation actualGameBoard = actual.getGameBoard();

    actualGameBoard.getGameboard().forEach(gb -> {
      gb.forEach(gs -> {
        assertEquals(0, gs.getLevels());
        assertNull(gs.getWorkerId());
      });
    });
  }

  private JsonSquareRepresentation emptySquare() {
    return new JsonSquareRepresentation(0, null);
  }
}
