package com.terence.santorini.game;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonGameSquare implements Serializable {
  private Integer levels = 0;
  private String workerId = null;

  public JsonGameSquare(Integer levels, String workerId) {
    this.levels = levels;
    this.workerId = workerId;
  }

  public JsonGameSquare() {}
}
