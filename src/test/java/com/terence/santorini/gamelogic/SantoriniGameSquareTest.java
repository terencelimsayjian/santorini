package com.terence.santorini.gamelogic;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SantoriniGameSquareTest {

  @Nested
  class AddWorker {
    @Test
    void shouldReturnEmptyOptionalIfNoWorkerExists() {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();
      assertFalse(santoriniGameSquare.getWorker().isPresent());
    }

    @Test
    void shouldAddWorker() throws GameBoardException {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();

      santoriniGameSquare.placeWorker(new SantoriniWorker("1"));

      assertTrue(santoriniGameSquare.getWorker().isPresent());
    }

    @Test
    void shouldAddWorkerToFirstLevel() throws GameBoardException {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();

      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeWorker(new SantoriniWorker("1"));

      assertTrue(santoriniGameSquare.getWorker().isPresent());
    }

    @Test
    void shouldAddWorkerToSecondLevel() throws GameBoardException {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();

      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeWorker(new SantoriniWorker("1"));

      assertTrue(santoriniGameSquare.getWorker().isPresent());
    }

    @Test
    void shouldAddWorkerToThirdLevel() throws GameBoardException {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();

      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeWorker(new SantoriniWorker("1"));

      assertTrue(santoriniGameSquare.getWorker().isPresent());
    }

    @Test
    void shouldThrowExceptionWhenAddWorkerToDomeLevel() throws GameBoardException {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();

      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();

      GameBoardException e = assertThrows(GameBoardException.class, () -> santoriniGameSquare.placeWorker(new SantoriniWorker("1")));
      assertEquals("Cannot place worker on a dome", e.getMessage());
    }
  }

  @Nested
  class AddBlocks {
    @Test
    void shouldCountEmptySquare() {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();

      assertEquals(0, santoriniGameSquare.getLevels());
    }

    @Test
    void shouldCountFirstLevel() throws GameBoardException {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();

      santoriniGameSquare.placeNextBlock();

      assertEquals(1, santoriniGameSquare.getLevels());
    }

    @Test
    void shouldCountSecondLevel() throws GameBoardException {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();

      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();

      assertEquals(2, santoriniGameSquare.getLevels());
    }

    @Test
    void shouldCountThirdLevel() throws GameBoardException {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();

      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();

      assertEquals(3, santoriniGameSquare.getLevels());
    }

    @Test
    void shouldCountDomeLevel() throws GameBoardException {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();

      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();

      assertEquals(4, santoriniGameSquare.getLevels());
    }

    @Test
    void shouldThrowExceptionIfBlockIsPlacedOnDome() throws GameBoardException {
      SantoriniGameSquare santoriniGameSquare = SantoriniGameSquare.initiateEmptySquare();

      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();
      santoriniGameSquare.placeNextBlock();

      GameBoardException e = assertThrows(GameBoardException.class, () -> santoriniGameSquare.placeNextBlock());

      assertEquals("Maximum block capacity reached", e.getMessage());
    }
  }

}
