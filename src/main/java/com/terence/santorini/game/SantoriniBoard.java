package com.terence.santorini.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SantoriniBoard {
  final List<List<SantoriniSquare>> gameBoard;
  final HashMap<String, GridPosition> playerIdGridPositionLookup;

  private SantoriniBoard() {
    gameBoard = new ArrayList<>(5);

    gameBoard.add(0, Arrays.asList(SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare()));
    gameBoard.add(1, Arrays.asList(SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare()));
    gameBoard.add(2, Arrays.asList(SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare()));
    gameBoard.add(3, Arrays.asList(SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare()));
    gameBoard.add(4, Arrays.asList(SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare(), SantoriniSquare.initiateEmptySquare()));

    playerIdGridPositionLookup = new HashMap<>();
  }

  public static SantoriniBoard initiateBoard() {
    return new SantoriniBoard();
  }

  // TODO: Cannot place block on square with player
  public void placeBlock(GridPosition grid, SantoriniWorker worker) throws GameBoardException {
    GridPosition workerGridPosition = playerIdGridPositionLookup.get(worker.getId());

    List<GridPosition> legalGridPositions = getLegalGridPositions(workerGridPosition);

    if (!legalGridPositions.contains(grid)) {
      throw new GameBoardException("Must place block on adjacent square");
    }

    SantoriniSquare santoriniSquare = getSquare(grid);
    santoriniSquare.placeNextBlock();
  }

  public void placeWorker(GridPosition grid, SantoriniWorker worker) throws GameBoardException {
    int rowIndex = grid.getRowIndex();
    int columnIndex = grid.getColumnIndex();

    List<SantoriniSquare> row = gameBoard.get(rowIndex);
    SantoriniSquare santoriniSquare = row.get(columnIndex);

    if (playerIdGridPositionLookup.get(worker.getId()) != null) {
      throw new GameBoardException("Worker already exsts");
    }

    playerIdGridPositionLookup.put(worker.getId(), grid);

    santoriniSquare.placeWorker(worker);
  }

  // TODO: Cannot move up more than one level
  public void moveWorker(GridPosition newGridPosition, SantoriniWorker worker) throws GameBoardException {
    int rowIndex = newGridPosition.getRowIndex();
    int columnIndex = newGridPosition.getColumnIndex();
    List<SantoriniSquare> row = gameBoard.get(rowIndex);
    SantoriniSquare squareToMoveTo = row.get(columnIndex);

    GridPosition currentGridPosition = playerIdGridPositionLookup.get(worker.getId());

    if (currentGridPosition == null) {
      throw new GameBoardException("Worker does not exist on board");
    }

    if (currentGridPosition == newGridPosition) {
      throw new GameBoardException("Cannot move worker to same square");
    }

    List<GridPosition> legalGridPositions = getLegalGridPositions(currentGridPosition);

    if (!legalGridPositions.contains(newGridPosition)) {
      throw new GameBoardException("Must move worker to adjacent square");
    }

    SantoriniSquare square = getSquare(currentGridPosition);

    square.removeWorker();
    squareToMoveTo.placeWorker(worker);
    playerIdGridPositionLookup.put(worker.getId(), newGridPosition);
  }

  List<GridPosition> getLegalGridPositions(GridPosition currentGridPosition) {
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

  public boolean isWin(SantoriniWorker worker) {
    GridPosition currentGridPosition = playerIdGridPositionLookup.get(worker.getId());
    SantoriniSquare square = getSquare(currentGridPosition);

    if (square.getWorker().isPresent() && square.countLevels() == 3) {
      return true;
    }

    return false;
  }

  private SantoriniSquare getSquare(GridPosition gridPosition) {
    return gameBoard.get(gridPosition.getRowIndex()).get(gridPosition.getColumnIndex());
  }

}
