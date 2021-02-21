package com.terence.santorini.gamelogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SantoriniGameBoard {
  List<List<SantoriniGameSquare>> gameBoard;
  HashMap<String, GridPosition> playerIdGridPositionLookup;
  Map<SantoriniPlayer, List<String>> playerWorkersInPlay;
  Map<SantoriniPlayer, List<String>> availablePlayerWorkerPool;
  SantoriniPlayer currentPlayer;
  SantoriniPlayer firstPlayer;
  boolean workerPlacementStepComplete = false;

  private SantoriniGameBoard() {
    this.gameBoard = emptyBoard();
    this.playerIdGridPositionLookup = new HashMap<>();
    this.currentPlayer = SantoriniPlayer.A;
    this.firstPlayer = SantoriniPlayer.A;

    this.availablePlayerWorkerPool = new HashMap<>();
    this.availablePlayerWorkerPool.put(SantoriniPlayer.A, List.of("A1", "A2"));
    this.availablePlayerWorkerPool.put(SantoriniPlayer.B, List.of("B1", "B2"));

    this.playerWorkersInPlay = new HashMap<>();
    this.playerWorkersInPlay.put(SantoriniPlayer.A, new ArrayList<>());
    this.playerWorkersInPlay.put(SantoriniPlayer.B, new ArrayList<>());
  }

  public static SantoriniGameBoard initiateBoard() {
    return new SantoriniGameBoard();
  }

  public void placeBlock(GridPosition grid, String workerId) throws GameBoardException {
    GridPosition workerGridPosition = playerIdGridPositionLookup.get(workerId);

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

  public void placeWorker(GridPosition grid, SantoriniPlayer player) throws GameBoardException {
    int rowIndex = grid.getRowIndex();
    int columnIndex = grid.getColumnIndex();

    List<SantoriniGameSquare> row = gameBoard.get(rowIndex);
    SantoriniGameSquare santoriniGameSquare = row.get(columnIndex);

    String workerId = getNextWorkerId(player);

    if (playerIdGridPositionLookup.get(workerId) != null) {
      throw new GameBoardException("Worker already exsts");
    }

    santoriniGameSquare.placeWorker(workerId);
    playerIdGridPositionLookup.put(workerId, grid);
    playerWorkersInPlay.get(player).add(workerId);
  }

  public void moveWorker(GridPosition newGridPosition, String workerId) throws GameBoardException {
    int rowIndex = newGridPosition.getRowIndex();
    int columnIndex = newGridPosition.getColumnIndex();
    List<SantoriniGameSquare> row = gameBoard.get(rowIndex);
    SantoriniGameSquare squareToMoveTo = row.get(columnIndex);

    GridPosition currentWorkerPosition = playerIdGridPositionLookup.get(workerId);

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
    squareToMoveTo.placeWorker(workerId);
    playerIdGridPositionLookup.put(workerId, newGridPosition);
  }

  private String getNextWorkerId(SantoriniPlayer player) throws GameBoardException {
    List<String> workers = this.playerWorkersInPlay.get(player);

    if (workers.size() >= this.availablePlayerWorkerPool.get(player).size()) {
      throw new GameBoardException("Maximum number of workers reached");
    }

    if (workers.size() == 0) {
      return this.availablePlayerWorkerPool.get(player).get(0);
    } else if (workers.size() == 1) {
      return this.availablePlayerWorkerPool.get(player).get(1);
    } else {
      throw new GameBoardException("Maximum number of workers reached");
    }
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

    return List.of(gp1, gp2, gp3, gp4, gp5, gp6, gp7, gp8).stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  public boolean isWin(String workerId) {
    GridPosition currentGridPosition = playerIdGridPositionLookup.get(workerId);
    SantoriniGameSquare square = getSquare(currentGridPosition);

    if (square.getWorker().isPresent() && square.getLevels() == 3) {
      return true;
    }

    return false;
  }

  private SantoriniGameSquare getSquare(GridPosition gridPosition) {
    return gameBoard.get(gridPosition.getRowIndex()).get(gridPosition.getColumnIndex());
  }

  private ArrayList<List<SantoriniGameSquare>> emptyBoard() {
    ArrayList<List<SantoriniGameSquare>> gameBoard = new ArrayList<>(5);

    gameBoard.add(0, emptyRow());
    gameBoard.add(1, emptyRow());
    gameBoard.add(2, emptyRow());
    gameBoard.add(3, emptyRow());
    gameBoard.add(4, emptyRow());

    return gameBoard;
  }

  private List<SantoriniGameSquare> emptyRow() {
    return Arrays.asList(
        SantoriniGameSquare.initiateEmptySquare(),
        SantoriniGameSquare.initiateEmptySquare(),
        SantoriniGameSquare.initiateEmptySquare(),
        SantoriniGameSquare.initiateEmptySquare(),
        SantoriniGameSquare.initiateEmptySquare());
  }
}
