package com.terence.santorini.game;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class JsonGameBoard implements Serializable {
  private List<List<JsonGameSquare>> gameboard;
}
