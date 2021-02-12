package com.terence.santorini.gamelogic;

import java.util.Optional;

public class SantoriniSquare {
  private SantoriniWorker santoriniWorker;
  private Integer levels = 0;

  private SantoriniSquare() {
  }

  public static SantoriniSquare initiateEmptySquare() {
    return new SantoriniSquare();
  }

  public void placeWorker(SantoriniWorker worker) throws GameBoardException {
    if (isDome()) {
      throw new GameBoardException("Cannot place worker on a dome");
    }

    if (santoriniWorker != null) {
      throw new GameBoardException("Another worker occupies this spot");
    }

    santoriniWorker = worker;
  }

  public Optional<SantoriniWorker> getWorker() {
    return Optional.ofNullable(santoriniWorker);
  }

  public void placeNextBlock() throws GameBoardException {
    if (isDome()) {
      throw new GameBoardException("Maximum block capacity reached");
    }

    levels++;
  }

  private boolean isDome() {
    return levels >= 4;
  }

  public int getLevels() {
    return levels;
  }

  public void removeWorker() {
    santoriniWorker = null;
  }
}
