package com.terence.santorini.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.terence.santorini.gamelogic.GameSerializer;
import org.springframework.stereotype.Service;

@Service
public class GameService {

  GameRepository gameRepository;

  GameSerializer gameSerializer;

  public GameService(GameRepository gameRepository, GameSerializer gameSerializer) {
    this.gameRepository = gameRepository;
    this.gameSerializer = gameSerializer;
  }

  public void makePlayerMove(GameCommand gameCommand) throws JsonProcessingException {

    // Find game
    // JSON to gameboard

    // map json game representation to game board

    // try to apply move

    // serialize back

    // save

    // Return response


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
