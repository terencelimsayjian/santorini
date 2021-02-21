package com.terence.santorini.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.EnumSource;

class SantoriniGameBoardTest {

  @Nested
  class PlaceBlocks {
    @ParameterizedTest
    @CsvFileSource(
        resources = {"/legal-moves.csv"},
        numLinesToSkip = 1)
    void shouldAllowLegalBlockPlacement(
        String workerPosition, int row, int column, String newPosition) throws GameBoardException {
      GridPosition placeBlockGridPosition = GridPosition.valueOf(newPosition);
      GridPosition workerGridPosition = GridPosition.valueOf(workerPosition);

      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(workerGridPosition, "A1");
      board.placeBlock(placeBlockGridPosition, "A1");

      List<List<SantoriniGameSquare>> gameBoard = board.gameBoard;

      assertEquals(1, gameBoard.get(row).get(column).getLevels());
    }

    @ParameterizedTest
    @CsvFileSource(
        resources = {"/illegal-moves.csv"},
        numLinesToSkip = 1)
    void shouldThrowExceptionIfWorkerIsNotAdjacentToPlacedBlock(
        String workerPosition, String newPosition) throws GameBoardException {
      GridPosition placeBlockGridPosition = GridPosition.valueOf(newPosition);
      GridPosition workerGridPosition = GridPosition.valueOf(workerPosition);

      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(workerGridPosition, "A1");

      GameBoardException e =
          assertThrows(
              GameBoardException.class, () -> board.placeBlock(placeBlockGridPosition, "A1"));
      assertEquals("Must place block on adjacent square", e.getMessage());
    }

    @Test
    void shouldAllowExceptionToBubbleUpIfBlockHeightExceeds() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.D2, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      assertThrows(GameBoardException.class, () -> board.placeBlock(GridPosition.D1, "A1"));
    }

    @ParameterizedTest
    @EnumSource(GridPosition.class)
    void shouldThrowExceptionIfPlaceBlockOnSquareWithAWorker(GridPosition gridPosition)
        throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(gridPosition, "A1");

      GameBoardException e =
          assertThrows(GameBoardException.class, () -> board.placeBlock(gridPosition, "A1"));
      assertEquals("Worker already occupies square", e.getMessage());
    }
  }

  @Nested
  class PlaceWorker {
    @ParameterizedTest
    @CsvFileSource(
        resources = {"/grid-data.csv"},
        numLinesToSkip = 1)
    void shouldPlaceWorkerInCorrectLocationInList(String santoriniGrid, int row, int column)
        throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      GridPosition grid = GridPosition.valueOf(santoriniGrid);
      board.placeWorker(grid, "A1");

      List<List<SantoriniGameSquare>> gameBoard = board.gameBoard;

      assertTrue(gameBoard.get(row).get(column).getWorker().isPresent());
    }

    @Test
    void shouldThrowExceptionIfPlacingWorkerThatAlreadyExistsOnTheBoard()
        throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.D1, "A1");
      GameBoardException e =
          assertThrows(GameBoardException.class, () -> board.placeWorker(GridPosition.D1, "A1"));
      assertEquals("Worker already exsts", e.getMessage());
    }

    @Test
    void shouldAllowExceptionToBubbleUpIfWorkerIsPlacedInOccupiedSlot() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.D1, "A1");
      assertThrows(GameBoardException.class, () -> board.placeWorker(GridPosition.D1, "A1"));
    }

    @Test
    void shouldAllowExceptionToBubbleUpIfWorkerIsPlacedOnDome() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.D2, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      assertThrows(GameBoardException.class, () -> board.placeWorker(GridPosition.D1, "A1"));
    }
  }

  @Nested
  class MoveWorker {
    @ParameterizedTest
    @CsvFileSource(resources = "/legal-moves.csv", numLinesToSkip = 1)
    void shouldMoveWorkerIfLegal(String workerPosition, int row, int column, String newPosition)
        throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      GridPosition newGridPosition = GridPosition.valueOf(newPosition);
      GridPosition originalGridPosition = GridPosition.valueOf(workerPosition);

      board.placeWorker(originalGridPosition, "A1");
      board.moveWorker(newGridPosition, "A1");

      List<List<SantoriniGameSquare>> gameBoard = board.gameBoard;

      // Assert worker has been removed from old square and placed on new square
      assertTrue(gameBoard.get(row).get(column).getWorker().isPresent());
      int oldRow = originalGridPosition.getRowIndex();
      int oldColumn = originalGridPosition.getColumnIndex();
      assertFalse(gameBoard.get(oldRow).get(oldColumn).getWorker().isPresent());
    }

    @ParameterizedTest
    @CsvFileSource(
        resources = {"/illegal-moves.csv"},
        numLinesToSkip = 1)
    void shouldThrowExceptionIfTryToMoveWorkerMoreThanOneSpotAway(
        String workerPosition, String newPosition) throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      GridPosition originalWorkerGridPosition = GridPosition.valueOf(workerPosition);
      GridPosition newGridPosition = GridPosition.valueOf(newPosition);

      board.placeWorker(originalWorkerGridPosition, "A1");

      GameBoardException e =
          assertThrows(GameBoardException.class, () -> board.moveWorker(newGridPosition, "A1"));
      assertEquals("Must move worker to adjacent square", e.getMessage());
    }

    @Test
    void shouldThrowExceptionIfWorkerAttemptsToMoveUpMoreThanOneBlockHeight()
        throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.B3, "A1");
      board.placeBlock(GridPosition.B4, "A1");
      board.placeBlock(GridPosition.B4, "A1");

      GameBoardException e =
          assertThrows(GameBoardException.class, () -> board.moveWorker(GridPosition.B4, "A1"));
      assertEquals("Worker can move up maximum of one level higher", e.getMessage());
    }

    @Test
    void shouldThrowExceptionIfWorkerIdIsNotFound() {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      GameBoardException e =
          assertThrows(GameBoardException.class, () -> board.moveWorker(GridPosition.B5, "A1"));
      assertEquals("Worker does not exist on board", e.getMessage());
    }

    @Test
    void shouldThrowExceptionIfTryToMoveWorkerToSameSpot() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.A1, "A1");
      GameBoardException e =
          assertThrows(GameBoardException.class, () -> board.moveWorker(GridPosition.A1, "A1"));
      assertEquals("Cannot move worker to same square", e.getMessage());
    }
  }

  @Nested
  class CheckWin {
    @Test
    void shouldReturnTrueWithBasicWinCondition() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.A1, "A1");
      board.placeBlock(GridPosition.A2, "A1");

      board.moveWorker(GridPosition.A2, "A1");
      board.placeBlock(GridPosition.A3, "A1");
      board.placeBlock(GridPosition.A3, "A1");

      board.moveWorker(GridPosition.A3, "A1");
      board.placeBlock(GridPosition.A4, "A1");
      board.placeBlock(GridPosition.A4, "A1");
      board.placeBlock(GridPosition.A4, "A1");

      board.moveWorker(GridPosition.A4, "A1");
      assertTrue(board.isWin("A1"));
    }

    @Test
    void shouldNotReturnTrueWithWorkerOnLevel2() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.A1, "A1");
      board.placeBlock(GridPosition.A2, "A1");

      board.moveWorker(GridPosition.A2, "A1");
      board.placeBlock(GridPosition.A3, "A1");
      board.placeBlock(GridPosition.A3, "A1");

      board.moveWorker(GridPosition.A3, "A1");
      assertFalse(board.isWin("A1"));
    }

    @Test
    void shouldNotReturnTrueWithWorkerOnLevel1() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.A1, "A1");
      board.placeBlock(GridPosition.A2, "A1");

      board.moveWorker(GridPosition.A2, "A1");
      assertFalse(board.isWin("A1"));
    }

    @Test
    void shouldNotReturnTrueWithWorkerOnLevel0() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.A1, "A1");
      board.moveWorker(GridPosition.A2, "A1");
      assertFalse(board.isWin("A1"));
    }
  }

  void generateData() {
    SantoriniGameBoard santoriniGameBoard = SantoriniGameBoard.initiateBoard();
    List<GridPosition> gridPositions = Arrays.asList(GridPosition.values());

    for (int i = 0; i < gridPositions.size(); i++) {

      GridPosition gridPosition = gridPositions.get(i);
      List<GridPosition> fullList = new ArrayList<>(gridPositions);
      List<GridPosition> legalGridPositions =
          santoriniGameBoard.getLegalGridPositions(gridPosition);

      List<GridPosition> collect =
          fullList.stream()
              .filter(a -> legalGridPositions.contains(a))
              .collect(Collectors.toList());

      for (int j = 0; j < collect.size(); j++) {
        GridPosition newPos = collect.get(j);
        if (!gridPosition.name().equals(newPos.name())) {
          System.out.println(
              gridPosition.name()
                  + ","
                  + newPos.getRowIndex()
                  + ","
                  + newPos.getColumnIndex()
                  + ","
                  + newPos.name());
        }
      }
    }
  }
}
