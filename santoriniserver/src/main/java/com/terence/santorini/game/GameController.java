package com.terence.santorini.game;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("/make-move")
  public @ResponseBody JsonGameBoard makePlayerMove(@RequestBody GameCommand gameCommand) {
    JsonGameBoard jsonGameBoard = gameService.makePlayerMove(gameCommand);

    return jsonGameBoard;
  }
}
