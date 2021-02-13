package com.terence.santorini.gamelogic;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class JsonGameRepresentation implements Serializable {
  private List<List<JsonSquareRepresentation>> gameboard;
}
