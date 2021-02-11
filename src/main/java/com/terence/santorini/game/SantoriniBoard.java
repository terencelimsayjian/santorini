package com.terence.santorini.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

          if (santoriniWorker.getId().equals(worker.getId())) {
            throw new GameBoardException("Worker already exsts");
          }

        }
      }
    }

    santoriniSquare.placeWorker(worker);
  }

  public void moveWorker(GridPosition newGridPosition, SantoriniWorker worker) throws GameBoardException {
    int rowIndex = newGridPosition.getRowIndex();
    int columnIndex = newGridPosition.getColumnIndex();
    List<SantoriniSquare> row = gameBoard.get(rowIndex);
    SantoriniSquare squareToMoveTo = row.get(columnIndex);

    for (int i = 0; i < gameBoard.size(); i++) {
      List<SantoriniSquare> santoriniSquares = gameBoard.get(i);

      for (int j = 0; j < santoriniSquares.size(); j++) {
        SantoriniSquare square = santoriniSquares.get(j);

        if (square.getWorker().isPresent()) {

          GridPosition currentGridPosition = GridPosition.from(i, j).get();

          if (currentGridPosition == newGridPosition) {
            throw new GameBoardException("Cannot move worker to same square");
          }

          List<GridPosition> legalGridPositions = getLegalGridPositions(currentGridPosition);

          if (!legalGridPositions.contains(newGridPosition)) {
            throw new GameBoardException("Can only move worker one square");
          }

          square.removeWorker();
          squareToMoveTo.placeWorker(worker);
          return;
        }
      }
    }

    throw new GameBoardException("Worker does not exist on board");
  }

  private List<GridPosition> getLegalGridPositions(GridPosition currentGridPosition) {
    int rowIndex = currentGridPosition.getRowIndex();
    int columnIndex = currentGridPosition.getColumnIndex();

    // Starting north and moving clockwise
    Optional<GridPosition> gp1 = GridPosition.from(rowIndex - 1, columnIndex);
    Optional<GridPosition> gp2 = GridPosition.from(rowIndex - 1, columnIndex + 1);
    Optional<GridPosition> gp3 = GridPosition.from(rowIndex, columnIndex + 1);
    Optional<GridPosition> gp4 = GridPosition.from(rowIndex + 1, columnIndex + 1);
    Optional<GridPosition> gp5 = GridPosition.from(rowIndex + 1, columnIndex);
    Optional<GridPosition> gp6 = GridPosition.from(rowIndex + 1, columnIndex - 1);
    Optional<GridPosition> gp7 = GridPosition.from(rowIndex, columnIndex - 1);
    Optional<GridPosition> gp8 = GridPosition.from(rowIndex - 1, columnIndex - 1);

    return List.of(gp1, gp2, gp3, gp4, gp5, gp6, gp7, gp8)
        .stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }


}
