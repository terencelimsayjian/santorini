package com.terence.santorini.gamelogic;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class JsonSquareRepresentation implements Serializable {
  private Integer levels = 0;
  private String workerId = null;

  public JsonSquareRepresentation(Integer levels, String workerId) {
    this.levels = levels;
    this.workerId = workerId;
  }

  public JsonSquareRepresentation() {
  }
}
