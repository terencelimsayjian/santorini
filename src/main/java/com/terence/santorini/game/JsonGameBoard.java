package com.terence.santorini.game;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonGameBoard implements Serializable {
  private List<List<JsonGameSquare>> gameboard;
}
