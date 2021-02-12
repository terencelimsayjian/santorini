package com.terence.santorini.gamelogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SantoriniGameBoard {
  List<List<SantoriniGameSquare>> gameBoard;
  HashMap<String, GridPosition> playerIdGridPositionLookup;

  private SantoriniGameBoard() {
    gameBoard = new ArrayList<>(5);

    gameBoard.add(0, Arrays.asList(SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare()));
    gameBoard.add(1, Arrays.asList(SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare()));
    gameBoard.add(2, Arrays.asList(SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare()));
    gameBoard.add(3, Arrays.asList(SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare()));
    gameBoard.add(4, Arrays.asList(SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare(), SantoriniGameSquare.initiateEmptySquare()));

    playerIdGridPositionLookup = new HashMap<>();
  }

  public static SantoriniGameBoard initiateBoard() {
    return new SantoriniGameBoard();
  }

  public static SantoriniGameBoard fromGameInProgress(List<List<SantoriniGameSquare>> gameBoardInProgress) {
    SantoriniGameBoard santoriniGameBoard = new SantoriniGameBoard();
    santoriniGameBoard.gameBoard = gameBoardInProgress;
    return santoriniGameBoard;
  }

  public void placeBlock(GridPosition grid, SantoriniWorker worker) throws GameBoardException {
    GridPosition workerGridPosition = playerIdGridPositionLookup.get(worker.getId());

    if (workerGridPosition == grid) {
      throw new GameBoardException("Worker already occupies square");
    }

    List<GridPosition> legalGridPositions = getLegalGridPositions(workerGridPosition);

    if (!legalGridPositions.contains(grid)) {
      throw new GameBoardException("Must place block on adjacent square");
    }

    SantoriniGameSquare santoriniGameSquare = getSquare(grid);
    santoriniGameSquare.placeNextBlock();
  }

  public void placeWorker(GridPosition grid, SantoriniWorker worker) throws GameBoardException {
    int rowIndex = grid.getRowIndex();
    int columnIndex = grid.getColumnIndex();

    List<SantoriniGameSquare> row = gameBoard.get(rowIndex);
    SantoriniGameSquare santoriniGameSquare = row.get(columnIndex);

    if (playerIdGridPositionLookup.get(worker.getId()) != null) {
      throw new GameBoardException("Worker already exsts");
    }

    playerIdGridPositionLookup.put(worker.getId(), grid);

    santoriniGameSquare.placeWorker(worker);
  }

  public void moveWorker(GridPosition newGridPosition, SantoriniWorker worker) throws GameBoardException {
    int rowIndex = newGridPosition.getRowIndex();
    int columnIndex = newGridPosition.getColumnIndex();
    List<SantoriniGameSquare> row = gameBoard.get(rowIndex);
    SantoriniGameSquare squareToMoveTo = row.get(columnIndex);

    GridPosition currentWorkerPosition = playerIdGridPositionLookup.get(worker.getId());

    if (currentWorkerPosition == null) {
      throw new GameBoardException("Worker does not exist on board");
    }

    if (currentWorkerPosition == newGridPosition) {
      throw new GameBoardException("Cannot move worker to same square");
    }

    List<GridPosition> legalGridPositions = getLegalGridPositions(currentWorkerPosition);

    if (!legalGridPositions.contains(newGridPosition)) {
      throw new GameBoardException("Must move worker to adjacent square");
    }

    SantoriniGameSquare currentWorkerSquare = getSquare(currentWorkerPosition);
    SantoriniGameSquare newSquare = getSquare(newGridPosition);

    if (newSquare.getLevels() - currentWorkerSquare.getLevels() > 1) {
      throw new GameBoardException("Worker can move up maximum of one level higher");
    }

    currentWorkerSquare.removeWorker();
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
    SantoriniGameSquare square = getSquare(currentGridPosition);

    if (square.getWorker().isPresent() && square.getLevels() == 3) {
      return true;
    }

    return false;
  }

  private SantoriniGameSquare getSquare(GridPosition gridPosition) {
    return gameBoard.get(gridPosition.getRowIndex()).get(gridPosition.getColumnIndex());
  }
}
