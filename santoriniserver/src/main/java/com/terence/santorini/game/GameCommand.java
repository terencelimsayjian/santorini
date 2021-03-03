package com.terence.santorini.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameCommand {

  private String id;

  private String workerId;

  private String command;

  private String originalGridPosition;

  private String newGridPosition;
}
