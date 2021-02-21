package com.terence.santorini.gamelogic;

import java.util.Optional;

public class SantoriniGameSquare {
  private Integer levels = 0;
  private String workerId;

  private SantoriniGameSquare() {}

  private SantoriniGameSquare(String workerId, Integer levels) {
    this.levels = levels;
    this.workerId = workerId;
  }

  public static SantoriniGameSquare initiateEmptySquare() {
    return new SantoriniGameSquare();
  }

  static SantoriniGameSquare fromExistingSquare(String workerId, Integer levels) {
    return new SantoriniGameSquare(workerId, levels);
  }

  public void placeWorker(String workerId) throws GameBoardException {
    if (isDome()) {
      throw new GameBoardException("Cannot place worker on a dome");
    }

    if (this.workerId != null) {
      throw new GameBoardException("Another worker occupies this spot");
    }

    this.workerId = workerId;
  }

  public Optional<String> getWorker() {
    return Optional.ofNullable(workerId);
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
    workerId = null;
  }
}
