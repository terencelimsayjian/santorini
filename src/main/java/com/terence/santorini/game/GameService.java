package com.terence.santorini.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.terence.santorini.gamelogic.SantoriniGameboardMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {

  private final GameRepository gameRepository;
  private final SantoriniGameboardMapper santoriniGameboardMapper;

  public GameService(GameRepository gameRepository, SantoriniGameboardMapper santoriniGameboardMapper) {
    this.gameRepository = gameRepository;
    this.santoriniGameboardMapper = santoriniGameboardMapper;
  }

  public void makePlayerMove(GameCommand gameCommand) throws JsonProcessingException {
    // Game should be created? I think game should be created when ppl join the room, not over here
    // This should be player actions

    Optional<Game> optionalGame = gameRepository.findById("ID");

    if (optionalGame.isEmpty()) {
      throw new RuntimeException();
    }

    Game game = optionalGame.get();

//    SantoriniGameBoard santoriniGameBoard = santoriniBoardSerializer.fromJsonString(game.getGameBoard());
//
//    try {
//      santoriniGameBoard.moveWorker(GridPosition.A1, new SantoriniWorker("A1"));
//    } catch (GameBoardException e) {
//      e.printStackTrace();
//    }
//
//
//    String updatedGameboardString = santoriniBoardSerializer.toJsonString(santoriniGameBoard);
//
//    game.setGameBoard(updatedGameboardString);
//
//    gameRepository.save(game);

    // Place worker
    // Move worker
    // Place block

    // Responses
    // OK // Continue
    // WIN
    // Invalid move
    // Not your turn

  }
}
