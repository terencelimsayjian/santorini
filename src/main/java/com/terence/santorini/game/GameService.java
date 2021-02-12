package com.terence.santorini.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.terence.santorini.gamelogic.SantoriniBoardSerializer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {

  private final GameRepository gameRepository;
  private final SantoriniBoardSerializer santoriniBoardSerializer;

  public GameService(GameRepository gameRepository, SantoriniBoardSerializer santoriniBoardSerializer) {
    this.gameRepository = gameRepository;
    this.santoriniBoardSerializer = santoriniBoardSerializer;
  }

  public void makePlayerMove(GameCommand gameCommand) throws JsonProcessingException {
    // Game should be created? I think game should be created when ppl join the room, not over here
    // This should be player actions

    Optional<Game> optionalGame = gameRepository.findById("ID");

    if (optionalGame.isEmpty()) {
      throw new RuntimeException();
    }

//    Game game = optionalGame.get();
//
//    JsonGameRepresentation jsonGameRepresentation = santoriniBoardSerializer.jsonToBean(game.getGameBoard());
//
//    SantoriniBoard santoriniBoard = mapper.jsonRepresentationToGameboard(jsonGameRepresentation);
//
//    try {
//      santoriniBoard.moveWorker(GridPosition.A1, new SantoriniWorker("A1"));
//    } catch (GameBoardException e) {
//      e.printStackTrace();
//    }
//
//    JsonGameRepresentation updatedJsonRep = mapper.gameboardToJsonRepresentation(santoriniBoard);
//
//    String updatedGameboardString = santoriniBoardSerializer.beanToJson(updatedJsonRep);
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
