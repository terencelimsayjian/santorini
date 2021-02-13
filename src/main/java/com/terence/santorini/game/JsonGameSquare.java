package com.terence.santorini.game;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class JsonGameSquare implements Serializable {
  private Integer levels = 0;
  private String workerId = null;

  public JsonGameSquare(Integer levels, String workerId) {
    this.levels = levels;
    this.workerId = workerId;
  }

  public JsonGameSquare() {
  }
}
