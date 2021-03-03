package com.terence.santorini.gamelogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.EnumSource;

class SantoriniGameBoardTest {

  @Test
  void initialisation() {
    SantoriniGameBoard santoriniGameBoard = SantoriniGameBoard.initiateBoard();

    // Verify all squares initialized empty
    List<List<SantoriniGameSquare>> gameBoard = santoriniGameBoard.gameBoard;
    gameBoard.stream()
        .flatMap(Collection::stream)
        .forEach(
            square -> {
              assertEquals(0, square.getLevels());
              assertFalse(square.getWorker().isPresent());
            });

    // Verify playerId lookup initialized
    HashMap<String, GridPosition> playerIdGridPositionLookup =
        santoriniGameBoard.playerIdGridPositionLookup;
    assertNotNull(playerIdGridPositionLookup);
    assertEquals(0, playerIdGridPositionLookup.keySet().size());

    // Verify first player and current player initialised to Player A
    assertEquals(SantoriniPlayer.A, santoriniGameBoard.currentPlayer);
    assertEquals(SantoriniPlayer.A, santoriniGameBoard.firstPlayer);
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
      board.placeWorker(grid, SantoriniPlayer.A);

      List<List<SantoriniGameSquare>> gameBoard = board.gameBoard;

      assertTrue(gameBoard.get(row).get(column).getWorker().isPresent());
      assertEquals("A1", gameBoard.get(row).get(column).getWorker().get());
      assertFalse(board.workerMovedAndAwaitingBlockPlacement);
      assertFalse(board.workerPlacementStepComplete);
    }

    @Test
    void shouldPlaceSecondWorkerOnBoardAndSwitchCurrentPlayer() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.D1, SantoriniPlayer.A);
      board.placeWorker(GridPosition.B1, SantoriniPlayer.A);

      List<List<SantoriniGameSquare>> gameBoard = board.gameBoard;
      assertTrue(
          gameBoard
              .get(GridPosition.D1.getRowIndex())
              .get(GridPosition.D1.getColumnIndex())
              .getWorker()
              .isPresent());
      assertEquals(
          "A1",
          gameBoard
              .get(GridPosition.D1.getRowIndex())
              .get(GridPosition.D1.getColumnIndex())
              .getWorker()
              .get());

      assertTrue(
          gameBoard
              .get(GridPosition.B1.getRowIndex())
              .get(GridPosition.B1.getColumnIndex())
              .getWorker()
              .isPresent());
      assertEquals(
          "A2",
          gameBoard
              .get(GridPosition.B1.getRowIndex())
              .get(GridPosition.B1.getColumnIndex())
              .getWorker()
              .get());

      assertEquals(SantoriniPlayer.B, board.currentPlayer);
      assertFalse(board.workerPlacementStepComplete);
      assertFalse(board.workerMovedAndAwaitingBlockPlacement);
    }

    @Test
    void shouldThrowExceptionIfMoreThanTwoWorkersPlacedOnBoard() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.D1, SantoriniPlayer.A);
      board.placeWorker(GridPosition.B1, SantoriniPlayer.A);
      GameBoardException e =
          assertThrows(
              GameBoardException.class,
              () -> board.placeWorker(GridPosition.E1, SantoriniPlayer.A));
      assertEquals("Maximum number of workers reached", e.getMessage());
      assertFalse(board.workerPlacementStepComplete);
      assertFalse(board.workerMovedAndAwaitingBlockPlacement);
    }

    @Test
    void shouldAllowSecondPlayerToPlaceTwoWorkersOnBoard() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.D1, SantoriniPlayer.A);
      board.placeWorker(GridPosition.B1, SantoriniPlayer.A);
      assertEquals(SantoriniPlayer.B, board.currentPlayer);

      board.placeWorker(GridPosition.E1, SantoriniPlayer.B);
      board.placeWorker(GridPosition.E2, SantoriniPlayer.B);
      assertEquals(SantoriniPlayer.A, board.currentPlayer);

      List<List<SantoriniGameSquare>> gameBoard = board.gameBoard;
      assertTrue(board.workerPlacementStepComplete);
      assertFalse(board.workerMovedAndAwaitingBlockPlacement);
      assertTrue(
          gameBoard
              .get(GridPosition.D1.getRowIndex())
              .get(GridPosition.D1.getColumnIndex())
              .getWorker()
              .isPresent());
      assertEquals(
          "A1",
          gameBoard
              .get(GridPosition.D1.getRowIndex())
              .get(GridPosition.D1.getColumnIndex())
              .getWorker()
              .get());

      assertTrue(
          gameBoard
              .get(GridPosition.B1.getRowIndex())
              .get(GridPosition.B1.getColumnIndex())
              .getWorker()
              .isPresent());
      assertEquals(
          "A2",
          gameBoard
              .get(GridPosition.B1.getRowIndex())
              .get(GridPosition.B1.getColumnIndex())
              .getWorker()
              .get());

      assertTrue(
          gameBoard
              .get(GridPosition.D1.getRowIndex())
              .get(GridPosition.D1.getColumnIndex())
              .getWorker()
              .isPresent());
      assertEquals(
          "B1",
          gameBoard
              .get(GridPosition.E1.getRowIndex())
              .get(GridPosition.E1.getColumnIndex())
              .getWorker()
              .get());

      assertTrue(
          gameBoard
              .get(GridPosition.B1.getRowIndex())
              .get(GridPosition.B1.getColumnIndex())
              .getWorker()
              .isPresent());
      assertEquals(
          "B2",
          gameBoard
              .get(GridPosition.E2.getRowIndex())
              .get(GridPosition.E2.getColumnIndex())
              .getWorker()
              .get());
    }

    @Test
    void shouldAllowExceptionToBubbleUpIfWorkerIsPlacedInOccupiedSlot() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.D1, SantoriniPlayer.A);
      assertThrows(
          GameBoardException.class, () -> board.placeWorker(GridPosition.D1, SantoriniPlayer.A));
    }

    @Test
    void shouldAllowExceptionToBubbleUpIfWorkerIsPlacedOnDome() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.D2, SantoriniPlayer.A);
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      assertThrows(
          GameBoardException.class, () -> board.placeWorker(GridPosition.D1, SantoriniPlayer.A));
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

      board.placeWorker(originalGridPosition, SantoriniPlayer.A);
      board.moveWorker(newGridPosition, "A1");

      List<List<SantoriniGameSquare>> gameBoard = board.gameBoard;

      // Assert worker has been removed from old square and placed on new square
      assertTrue(gameBoard.get(row).get(column).getWorker().isPresent());
      int oldRow = originalGridPosition.getRowIndex();
      int oldColumn = originalGridPosition.getColumnIndex();
      assertFalse(gameBoard.get(oldRow).get(oldColumn).getWorker().isPresent());
      assertTrue(board.workerMovedAndAwaitingBlockPlacement);
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

      board.placeWorker(originalWorkerGridPosition, SantoriniPlayer.A);

      GameBoardException e =
          assertThrows(GameBoardException.class, () -> board.moveWorker(newGridPosition, "A1"));
      assertEquals("Must move worker to adjacent square", e.getMessage());
      assertFalse(board.workerMovedAndAwaitingBlockPlacement);
    }

    @Test
    void shouldThrowExceptionIfWorkerAttemptsToMoveUpMoreThanOneBlockHeight()
        throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.B3, SantoriniPlayer.A);
      board.placeBlock(GridPosition.B4, "A1");
      board.placeBlock(GridPosition.B4, "A1");

      GameBoardException e =
          assertThrows(GameBoardException.class, () -> board.moveWorker(GridPosition.B4, "A1"));
      assertEquals("Worker can move up maximum of one level higher", e.getMessage());
      assertFalse(board.workerMovedAndAwaitingBlockPlacement);
    }

    @Test
    void shouldThrowExceptionIfWorkerIdIsNotFound() {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      GameBoardException e =
          assertThrows(GameBoardException.class, () -> board.moveWorker(GridPosition.B5, "A1"));
      assertEquals("Worker does not exist on board", e.getMessage());
      assertFalse(board.workerMovedAndAwaitingBlockPlacement);
    }

    @Test
    void shouldThrowExceptionIfTryToMoveWorkerToSameSpot() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.A1, SantoriniPlayer.A);
      GameBoardException e =
          assertThrows(GameBoardException.class, () -> board.moveWorker(GridPosition.A1, "A1"));
      assertEquals("Cannot move worker to same square", e.getMessage());
      assertFalse(board.workerMovedAndAwaitingBlockPlacement);
    }
  }

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

      board.placeWorker(placeBlockGridPosition, SantoriniPlayer.A);
      board.moveWorker(workerGridPosition, "A1");
      board.placeBlock(placeBlockGridPosition, "A1");

      List<List<SantoriniGameSquare>> gameBoard = board.gameBoard;

      assertEquals(1, gameBoard.get(row).get(column).getLevels());
      assertEquals(SantoriniPlayer.B, board.currentPlayer);
      assertFalse(board.workerMovedAndAwaitingBlockPlacement);
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

      board.placeWorker(workerGridPosition, SantoriniPlayer.A);

      GameBoardException e =
          assertThrows(
              GameBoardException.class, () -> board.placeBlock(placeBlockGridPosition, "A1"));
      assertEquals("Must place block on adjacent square", e.getMessage());
      assertEquals(SantoriniPlayer.A, board.currentPlayer);
    }

    @Test
    void shouldAllowExceptionToBubbleUpIfBlockHeightExceeds() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.D1, SantoriniPlayer.A);
      board.moveWorker(GridPosition.D2, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      board.placeBlock(GridPosition.D1, "A1");
      assertThrows(GameBoardException.class, () -> board.placeBlock(GridPosition.D1, "A1"));
      assertEquals(SantoriniPlayer.A, board.currentPlayer);
    }

    @ParameterizedTest
    @EnumSource(GridPosition.class)
    void shouldThrowExceptionIfPlaceBlockOnSquareWithAWorker(GridPosition gridPosition)
        throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(gridPosition, SantoriniPlayer.A);

      GameBoardException e =
          assertThrows(GameBoardException.class, () -> board.placeBlock(gridPosition, "A1"));
      assertEquals("Worker already occupies square", e.getMessage());
      assertEquals(SantoriniPlayer.A, board.currentPlayer);
    }
  }

  @Nested
  class CheckWin {
    @Test
    void shouldReturnTrueWithBasicWinCondition() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.A1, SantoriniPlayer.A);
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

      board.placeWorker(GridPosition.A1, SantoriniPlayer.A);
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

      board.placeWorker(GridPosition.A1, SantoriniPlayer.A);
      board.placeBlock(GridPosition.A2, "A1");

      board.moveWorker(GridPosition.A2, "A1");
      assertFalse(board.isWin("A1"));
    }

    @Test
    void shouldNotReturnTrueWithWorkerOnLevel0() throws GameBoardException {
      SantoriniGameBoard board = SantoriniGameBoard.initiateBoard();

      board.placeWorker(GridPosition.A1, SantoriniPlayer.A);
      board.moveWorker(GridPosition.A2, "A1");
      assertFalse(board.isWin("A1"));
    }
  }
}
