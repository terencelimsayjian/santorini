package com.terence.santorini.game;

public class SantoriniGame {
  private final SantoriniBoard santoriniBoard;
//  Player 1
  // PLayer 2
//  Players have workers
  // Player currentPlayer

  private SantoriniGame() {
    santoriniBoard = SantoriniBoard.initiateBoard();
  }

  public static SantoriniGame newGame() { // Players
    return new SantoriniGame();
  }

  public void switchActivePlayer() {

  }

  public void moveWorker(GridPosition grid, SantoriniWorker worker) {

  }

  public void isWin() {

  }


}
