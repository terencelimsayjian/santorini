package com.terence.santorini.game;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SantoriniBoardTest {

  @Nested
  class PlaceBlocks {
    @ParameterizedTest
    @CsvFileSource(resources = {"/grid-data.csv"}, numLinesToSkip = 1)
    void shouldPlaceBlocksInCorrectLocationInList(String santoriniGrid, int row, int column) throws GameBoardException {
      SantoriniBoard board = SantoriniBoard.initiateBoard();

      GridPosition grid = GridPosition.valueOf(santoriniGrid);
      board.placeBlock(grid);

      List<List<SantoriniSquare>> gameBoard = board.gameBoard;

      assertEquals(1, gameBoard.get(row).get(column).countLevels());
    }

    @Test
    void shouldAllowExceptionToBubbleUpIfBlockHeightExceeds() throws GameBoardException {
      SantoriniBoard board = SantoriniBoard.initiateBoard();

      board.placeBlock(GridPosition.D1);
      board.placeBlock(GridPosition.D1);
      board.placeBlock(GridPosition.D1);
      board.placeBlock(GridPosition.D1);
      assertThrows(GameBoardException.class, () -> board.placeBlock(GridPosition.D1));
    }

  }


  @Nested
  class PlaceWorker {
    @ParameterizedTest
    @CsvFileSource(resources = {"/grid-data.csv"}, numLinesToSkip = 1)
    void shouldPlaceWorkerInCorrectLocationInList(String santoriniGrid, int row, int column) throws GameBoardException {
      SantoriniBoard board = SantoriniBoard.initiateBoard();

      GridPosition grid = GridPosition.valueOf(santoriniGrid);
      board.placeWorker(grid, new SantoriniWorker("1"));

      List<List<SantoriniSquare>> gameBoard = board.gameBoard;

      assertTrue(gameBoard.get(row).get(column).getWorker().isPresent());
    }

    @Test
    void shouldThrowExceptionIfPlacingWorkerThatAlreadyExistsOnTheBoard() throws GameBoardException {
      SantoriniBoard board = SantoriniBoard.initiateBoard();

      board.placeWorker(GridPosition.D1, new SantoriniWorker("1"));
      GameBoardException e = assertThrows(GameBoardException.class, () -> board.placeWorker(GridPosition.D1, new SantoriniWorker("1")));
      assertEquals("Worker already exsts", e.getMessage());
    }

    @Test
    void shouldAllowExceptionToBubbleUpIfWorkerIsPlacedInOccupiedSlot() throws GameBoardException {
      SantoriniBoard board = SantoriniBoard.initiateBoard();

      board.placeWorker(GridPosition.D1, new SantoriniWorker("1"));
      assertThrows(GameBoardException.class, () -> board.placeWorker(GridPosition.D1, new SantoriniWorker("1")));
    }

    @Test
    void shouldAllowExceptionToBubbleUpIfWorkerIsPlacedOnDome() throws GameBoardException {
      SantoriniBoard board = SantoriniBoard.initiateBoard();

      board.placeBlock(GridPosition.D1);
      board.placeBlock(GridPosition.D1);
      board.placeBlock(GridPosition.D1);
      board.placeBlock(GridPosition.D1);
      assertThrows(GameBoardException.class, () -> board.placeWorker(GridPosition.D1, new SantoriniWorker("1")));
    }
  }

  @Nested
  class MoveWorker {
    @ParameterizedTest
    @CsvFileSource(resources = "/grid-data.csv", numLinesToSkip = 2)
    void shouldMoveWorker(String newGridPosition, int row, int column, String startingGridPosition) throws GameBoardException {
      SantoriniWorker worker = new SantoriniWorker("1");
      SantoriniBoard board = SantoriniBoard.initiateBoard();

      // Excluded from CSV for this test case
      GridPosition startingPosition = GridPosition.valueOf(startingGridPosition);
      board.placeWorker(startingPosition, worker);

      GridPosition newPosition = GridPosition.valueOf(newGridPosition);
      board.moveWorker(newPosition, worker);

      List<List<SantoriniSquare>> gameBoard = board.gameBoard;

      // Assert worker has been removed from old square and placed on new square
      assertTrue(gameBoard.get(row).get(column).getWorker().isPresent());
      assertFalse(gameBoard.get(0).get(0).getWorker().isPresent());
    }

    @Test
    void shouldThrowExceptionIfWorkerIdIsNotFound() {
      SantoriniWorker worker = new SantoriniWorker("1");
      SantoriniBoard board = SantoriniBoard.initiateBoard();

      GameBoardException e = assertThrows(GameBoardException.class, () -> board.moveWorker(GridPosition.B5, worker));
      assertEquals("Worker does not exist on board", e.getMessage());
    }

    @Test
    void shouldThrowExceptionIfTryToMoveWorkerToSameSpot() throws GameBoardException {
      SantoriniWorker worker = new SantoriniWorker("1");
      SantoriniBoard board = SantoriniBoard.initiateBoard();

      board.placeWorker(GridPosition.A1, worker);
      GameBoardException e = assertThrows(GameBoardException.class, () -> board.moveWorker(GridPosition.A1, worker));
      assertEquals("Cannot move worker to same square", e.getMessage());
    }

    @Test
    void shouldThrowExceptionIfTryToMoveWorkerMoreThanOneSpotAway() throws GameBoardException {
      SantoriniWorker worker = new SantoriniWorker("1");
      SantoriniBoard board = SantoriniBoard.initiateBoard();

      // Excluded from CSV for this test case
      board.placeWorker(GridPosition.A1, worker);

      GameBoardException e = assertThrows(GameBoardException.class, () -> board.moveWorker(GridPosition.A3, worker));
      assertEquals("Can only move worker one square", e.getMessage());

    }
  }

}
