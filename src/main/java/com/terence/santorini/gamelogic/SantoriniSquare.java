package com.terence.santorini.gamelogic;

import java.util.Optional;

public class SantoriniSquare {
  private SantoriniWorker santoriniWorker;
  private boolean firstLevel = false;
  private boolean secondLevel = false;
  private boolean thirdLevel = false;
  private boolean dome = false;

  private SantoriniSquare() {
  }

  public static SantoriniSquare initiateEmptySquare() {
    return new SantoriniSquare();
  }

  public void placeWorker(SantoriniWorker worker) throws GameBoardException {
    if (dome == true) {
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
    if (dome) {
      throw new GameBoardException("Maximum block capacity reached");
    }

    if (!firstLevel) {
      firstLevel = true;
      return;
    }

    if (!secondLevel) {
      secondLevel = true;
      return;
    }

    if (!thirdLevel) {
      thirdLevel = true;
      return;
    }

    if (!dome) {
      dome = true;
      return;
    }
  }

  public int countLevels() {
    if (dome) {
      return 4;
    }

    if (thirdLevel) {
      return 3;
    }

    if (secondLevel) {
      return 2;
    }

    if (firstLevel) {
      return 1;
    }

    return 0;
  }

  public void removeWorker() {
    santoriniWorker = null;
  }
}
