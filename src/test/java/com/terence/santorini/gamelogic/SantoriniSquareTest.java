package com.terence.santorini.gamelogic;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SantoriniSquareTest {

  @Nested
  class AddWorker {
    @Test
    void shouldReturnEmptyOptionalIfNoWorkerExists() {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();
      assertFalse(santoriniSquare.getWorker().isPresent());
    }

    @Test
    void shouldAddWorker() throws GameBoardException {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();

      santoriniSquare.placeWorker(new SantoriniWorker("1"));

      assertTrue(santoriniSquare.getWorker().isPresent());
    }

    @Test
    void shouldAddWorkerToFirstLevel() throws GameBoardException {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();

      santoriniSquare.placeNextBlock();
      santoriniSquare.placeWorker(new SantoriniWorker("1"));

      assertTrue(santoriniSquare.getWorker().isPresent());
    }

    @Test
    void shouldAddWorkerToSecondLevel() throws GameBoardException {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();

      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();
      santoriniSquare.placeWorker(new SantoriniWorker("1"));

      assertTrue(santoriniSquare.getWorker().isPresent());
    }

    @Test
    void shouldAddWorkerToThirdLevel() throws GameBoardException {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();

      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();
      santoriniSquare.placeWorker(new SantoriniWorker("1"));

      assertTrue(santoriniSquare.getWorker().isPresent());
    }

    @Test
    void shouldThrowExceptionWhenAddWorkerToDomeLevel() throws GameBoardException {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();

      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();

      GameBoardException e = assertThrows(GameBoardException.class, () -> santoriniSquare.placeWorker(new SantoriniWorker("1")));
      assertEquals("Cannot place worker on a dome", e.getMessage());
    }
  }

  @Nested
  class AddBlocks {
    @Test
    void shouldCountEmptySquare() {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();

      assertEquals(0, santoriniSquare.getLevels());
    }

    @Test
    void shouldCountFirstLevel() throws GameBoardException {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();

      santoriniSquare.placeNextBlock();

      assertEquals(1, santoriniSquare.getLevels());
    }

    @Test
    void shouldCountSecondLevel() throws GameBoardException {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();

      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();

      assertEquals(2, santoriniSquare.getLevels());
    }

    @Test
    void shouldCountThirdLevel() throws GameBoardException {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();

      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();

      assertEquals(3, santoriniSquare.getLevels());
    }

    @Test
    void shouldCountDomeLevel() throws GameBoardException {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();

      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();

      assertEquals(4, santoriniSquare.getLevels());
    }

    @Test
    void shouldThrowExceptionIfBlockIsPlacedOnDome() throws GameBoardException {
      SantoriniSquare santoriniSquare = SantoriniSquare.initiateEmptySquare();

      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();
      santoriniSquare.placeNextBlock();

      GameBoardException e = assertThrows(GameBoardException.class, () -> santoriniSquare.placeNextBlock());

      assertEquals("Maximum block capacity reached", e.getMessage());
    }
  }

}
