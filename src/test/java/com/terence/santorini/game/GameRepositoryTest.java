package com.terence.santorini.game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GameRepositoryTest {

  @Autowired
  private GameRepository gameRepository;

  @Test
  void testSimpleSaveAndFind() {
    Game game = new Game();
    game.setGameBoard("GameBoard");
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

    assertEquals("GameBoard", actual.getGameBoard());
    assertEquals("Player1", actual.getPlayer1());
    assertEquals("Player2", actual.getPlayer2());
    assertEquals("Winner", actual.getWinner());
    assertEquals(createdAt, actual.getCreatedAt());
    assertEquals(updatedAt, actual.getUpdatedAt());
  }
}
