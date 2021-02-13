package com.terence.santorini.game;

import com.terence.santorini.gamelogic.JsonSquareRepresentation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class GameRepositoryTest {

  @Autowired
  private GameRepository gameRepository;

  @Test
  void testSimpleSaveAndFind() {

//    List<List<JsonSquareRepresentation>> jsonSquares = new ArrayList<>(5);
//    jsonSquares.add(0, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
//    jsonSquares.add(1, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
//    jsonSquares.add(2, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
//    jsonSquares.add(3, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
//    jsonSquares.add(4, Arrays.asList(emptySquare(), emptySquare(), emptySquare(), emptySquare(), emptySquare()));
//
//    JsonGameRepresentation jsonGameRepresentation = new JsonGameRepresentation();
//    jsonGameRepresentation.setGameboard(jsonSquares);

    Game game = new Game();
//    game.setGameBoard(jsonGameRepresentation);
    game.setPlayer1("Player1");
    game.setPlayer2("Player2");
    game.setWinner("Winner");
    Instant createdAt = Instant.now();
    game.setCreatedAt(createdAt);
    Instant updatedAt = Instant.now().plus(1000, ChronoUnit.SECONDS);
    game.setUpdatedAt(updatedAt);

    Game savedGame = gameRepository.save(game);

    Optional<Game> optionalGame = gameRepository.findById(savedGame.getId());

    assertTrue(optionalGame.isPresent());
    Game actual = optionalGame.get();

    assertEquals("Player1", actual.getPlayer1());
    assertEquals("Player2", actual.getPlayer2());
    assertEquals("Winner", actual.getWinner());
    assertEquals(createdAt, actual.getCreatedAt());
    assertEquals(updatedAt, actual.getUpdatedAt());

//    JsonGameRepresentation actualGameBoard = actual.getGameBoard();

//    actualGameBoard.getGameboard().forEach(gb -> {
//      gb.forEach(gs -> {
//        assertEquals(0, gs.getLevels());
//        assertNull(gs.getWorkerId());
//      });
//    });
  }

  private JsonSquareRepresentation emptySquare() {
    return new JsonSquareRepresentation(0, null);
  }
}
