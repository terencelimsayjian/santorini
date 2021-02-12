package com.terence.santorini.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonSquareRepresentation {
  public JsonSquareRepresentation() {
  }

  public JsonSquareRepresentation(Integer levels, String workerId) {
    this.levels = levels;
    this.workerId = workerId;
  }

  private Integer levels;
  private String workerId;
}
