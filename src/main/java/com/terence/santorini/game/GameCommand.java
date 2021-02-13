package com.terence.santorini.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameCommand {

  private String id;

  private String workerId;

  private String command; // PLACE_WORKER, MOVE_WORKER, PLACE_BLOCK, NO_ACTION

  private String originalGridPosition;

  private String newGridPosition;

}
