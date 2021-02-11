package com.terence.santorini.gamelogic;

import java.util.ArrayList;
import java.util.List;

public class SantoriniGame {
  private final SantoriniBoard board;
  private final Player player1;
  private final Player player2;
  private final List<SantoriniWorker> player1Workers;
  private final List<SantoriniWorker> player2Workers;

  private Player currentPlayer;
  //  Players have workers


  private SantoriniGame(Player player1, Player player2) {
    this.player1 = player1;
    this.player2 = player2;
    currentPlayer = player1;
    player1Workers = new ArrayList<>();
    player2Workers = new ArrayList<>();
    board = SantoriniBoard.initiateBoard();
  }

  public static SantoriniGame newGame(Player player1, Player player2) {
    return new SantoriniGame(player1, player2);
  }

  public void placeWorker() {

  }

  public void moveWorker() {
//     Game rule validation,
  }

  public void placeBlock() {

  }

  public void isWin() {

  }


}
