package com.terence.santorini.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SantoriniBoard {
  final List<List<SantoriniSquare>> gameBoard;

  private SantoriniBoard() {
    gameBoard = new ArrayList<>(5);

    gameBoard.add(0, Arrays.asList(SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare()));
    gameBoard.add(1, Arrays.asList(SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare()));
    gameBoard.add(2, Arrays.asList(SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare()));
    gameBoard.add(3, Arrays.asList(SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare()));
    gameBoard.add(4, Arrays.asList(SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare()));
  }

  public static SantoriniBoard initiateBoard() {
    return new SantoriniBoard();
  }

  public void placeBlock(GridPosition grid) throws GameBoardException {
    int rowIndex = grid.getRowIndex();
    int columnIndex = grid.getColumnIndex();

    List<SantoriniSquare> row = gameBoard.get(rowIndex);
    SantoriniSquare santoriniSquare = row.get(columnIndex);

    santoriniSquare.placeNextBlock();
  }

  public void placeWorker(GridPosition grid, SantoriniWorker worker) throws GameBoardException {
    int rowIndex = grid.getRowIndex();
    int columnIndex = grid.getColumnIndex();

    List<SantoriniSquare> row = gameBoard.get(rowIndex);
    SantoriniSquare santoriniSquare = row.get(columnIndex);

    for (int i = 0; i < gameBoard.size(); i++) {
      List<SantoriniSquare> santoriniSquares = gameBoard.get(i);

      for (int j = 0; j < santoriniSquares.size(); j++) {
        SantoriniSquare square = santoriniSquares.get(j);

        if (square.getWorker().isPresent()) {
          SantoriniWorker santoriniWorker = square.getWorker().get();

          if (santoriniWorker.getId() == worker.getId()) {
            throw new GameBoardException("Worker already exsts");
          }

        }
      }
    }

    santoriniSquare.placeWorker(worker);
  }

  public void moveWorker(GridPosition grid, SantoriniWorker worker) throws GameBoardException {
    int rowIndex = grid.getRowIndex();
    int columnIndex = grid.getColumnIndex();
    List<SantoriniSquare> row = gameBoard.get(rowIndex);
    SantoriniSquare squareToMoveTo = row.get(columnIndex);

    for (int i = 0; i < gameBoard.size(); i++) {
      List<SantoriniSquare> santoriniSquares = gameBoard.get(i);

      for (int j = 0; j < santoriniSquares.size(); j++) {
        SantoriniSquare square = santoriniSquares.get(j);

        if (square.getWorker().isPresent()) {

          if (GridPosition.from(i, j) == grid) {
            throw new GameBoardException("Cannot move worker to same square");
          }

          square.removeWorker();
          squareToMoveTo.placeWorker(worker);
          return;
        }
      }
    }

    throw new GameBoardException("Worker does not exist on board");
  }


}
