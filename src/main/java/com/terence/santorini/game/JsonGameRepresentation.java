package com.terence.santorini.game;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JsonGameRepresentation {
  private List<List<JsonSquareRepresentation>> gameboard;
}
