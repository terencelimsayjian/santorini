package com.terence.santorini.game;

import com.terence.santorini.gamelogic.GameBoardException;
import com.terence.santorini.gamelogic.GridPosition;
import com.terence.santorini.gamelogic.SantoriniGameBoard;
import com.terence.santorini.gamelogic.SantoriniGameboardMapper;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GameService {

  private final GameRepository gameRepository;
  private final SantoriniGameboardMapper santoriniGameboardMapper;

  public GameService(
      GameRepository gameRepository, SantoriniGameboardMapper santoriniGameboardMapper) {
    this.gameRepository = gameRepository;
    this.santoriniGameboardMapper = santoriniGameboardMapper;
  }

  public JsonGameBoard makePlayerMove(GameCommand gameCommand) {
    // Game should be created? I think game should be created when ppl join the room, not over here
    // This should be player actions

    Optional<GameEntity> optionalGame = gameRepository.findById(gameCommand.getId());

    // Temporary game creation code
    if (optionalGame.isEmpty()) {
      SantoriniGameBoard santoriniGameBoard = SantoriniGameBoard.initiateBoard();
      JsonGameBoard jsonGameBoard =
          santoriniGameboardMapper.gameboardToJsonRepresentation(santoriniGameBoard);
      GameEntity newGameEntity = new GameEntity();
      newGameEntity.setGameBoard(jsonGameBoard);
      GameEntity save = gameRepository.save(newGameEntity);
      optionalGame = Optional.of(save);
    }

    GameEntity gameEntity = optionalGame.get();

    SantoriniGameBoard santoriniGameBoard =
        santoriniGameboardMapper.jsonRepresentationToGameboard(gameEntity.getGameBoard());

    try {
      GridPosition newPosition = GridPosition.valueOf(gameCommand.getNewGridPosition());
      String workerId = gameCommand.getWorkerId();
      String command = gameCommand.getCommand();

      switch (command) {
        case "PLACE_WORKER":
          santoriniGameBoard.placeWorker(newPosition, workerId);
          break;
        case "MOVE_WORKER":
          santoriniGameBoard.moveWorker(newPosition, workerId);
          break;
        case "PLACE_BLOCK":
          santoriniGameBoard.placeBlock(newPosition, workerId);
          break;
        default:
          break;
      }

    } catch (GameBoardException e) {
      e.printStackTrace();
    }

    JsonGameBoard jsonGameBoard =
        santoriniGameboardMapper.gameboardToJsonRepresentation(santoriniGameBoard);

    gameEntity.setGameBoard(jsonGameBoard);

    gameRepository.save(gameEntity);

    return jsonGameBoard;

    // Responses
    // OK // Continue
    // WIN
    // Invalid move
    // Not your turn

  }
}
