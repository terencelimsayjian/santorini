package com.terence.santorini.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SantoriniBoardTest {


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

  @ParameterizedTest
  @CsvFileSource(resources = {"/grid-data.csv"}, numLinesToSkip = 1)
  void shouldPlaceWorkerInCorrectLocationInList(String santoriniGrid, int row, int column) throws GameBoardException {
    SantoriniBoard board = SantoriniBoard.initiateBoard();

    GridPosition grid = GridPosition.valueOf(santoriniGrid);
    board.placeWorker(grid, new SantoriniWorker());

    List<List<SantoriniSquare>> gameBoard = board.gameBoard;

    assertTrue(gameBoard.get(row).get(column).getWorker().isPresent());
  }

  @Test
  void shouldAllowExceptionToBubbleUpIfWorkerIsPlacedInOccupiedSlot() throws GameBoardException {
    SantoriniBoard board = SantoriniBoard.initiateBoard();

    board.placeWorker(GridPosition.D1, new SantoriniWorker());
    assertThrows(GameBoardException.class, () -> board.placeWorker(GridPosition.D1, new SantoriniWorker()));
  }

  @Test
  void shouldAllowExceptionToBubbleUpIfWorkerIsPlacedOnDome() throws GameBoardException {
    SantoriniBoard board = SantoriniBoard.initiateBoard();

    board.placeBlock(GridPosition.D1);
    board.placeBlock(GridPosition.D1);
    board.placeBlock(GridPosition.D1);
    board.placeBlock(GridPosition.D1);
    assertThrows(GameBoardException.class, () -> board.placeWorker(GridPosition.D1, new SantoriniWorker()));
  }
}
