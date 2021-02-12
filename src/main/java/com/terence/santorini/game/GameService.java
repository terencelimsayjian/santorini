package com.terence.santorini.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.terence.santorini.gamelogic.GameBoardException;
import com.terence.santorini.gamelogic.GameSerializer;
import com.terence.santorini.gamelogic.GridPosition;
import com.terence.santorini.gamelogic.JsonGameRepresentation;
import com.terence.santorini.gamelogic.JsonGameBoardMapper;
import com.terence.santorini.gamelogic.SantoriniBoard;
import com.terence.santorini.gamelogic.SantoriniWorker;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {

  private final GameRepository gameRepository;
  private final GameSerializer gameSerializer;
  private final JsonGameBoardMapper mapper;

  public GameService(GameRepository gameRepository, GameSerializer gameSerializer, JsonGameBoardMapper mapper) {
    this.gameRepository = gameRepository;
    this.gameSerializer = gameSerializer;
    this.mapper = mapper;
  }

  public void makePlayerMove(GameCommand gameCommand) throws JsonProcessingException {
    // Game should be created? I think game should be created when ppl join the room, not over here
    // This should be player actions

    Optional<Game> optionalGame = gameRepository.findById("ID");

    if (optionalGame.isEmpty()) {
      throw new RuntimeException();
    }

    Game game = optionalGame.get();

    JsonGameRepresentation jsonGameRepresentation = gameSerializer.jsonToBean(game.getGameBoard());

    SantoriniBoard santoriniBoard = mapper.jsonRepresentationToGameboard(jsonGameRepresentation);

    try {
      santoriniBoard.moveWorker(GridPosition.A1, new SantoriniWorker("A1"));
    } catch (GameBoardException e) {
      e.printStackTrace();
    }

    JsonGameRepresentation updatedJsonRep = mapper.gameboardToJsonRepresentation(santoriniBoard);

    String updatedGameboardString = gameSerializer.beanToJson(updatedJsonRep);

    game.setGameBoard(updatedGameboardString);

    gameRepository.save(game);

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
