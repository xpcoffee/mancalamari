package org.mancalamari.reducers;

import org.junit.jupiter.api.Test;
import org.mancalamari.game.Board;
import org.mancalamari.game.NormalPit;
import org.mancalamari.game.Pit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CaptureReducerTest {
    String playerOne = "playerOne";
    String playerTwo = "playerTwo";

    @Test
    void reduce_returnsEarlyIfLastPitWasNotEmpty() {
        Board board = Board.getStartingBoard(playerOne, playerTwo);
        SowReducer.Result sowResult = new SowReducer.Result(
                playerOne,
                board,
                0,
                false,
                false
        );

        CaptureReducer.Result result = new CaptureReducer().reduce(sowResult);

        assertSame(board, result.getBoard()); // board should not have been updated
    }

    @Test
    void reduce_capturesSeedsForPlayerOne() {
        int lastPitSowedIndex = 2;
        int oppositePitIndex = 10;

        List<Pit> pits = Board.getStartingBoard(playerOne, playerTwo).getPits();
        pits.set(lastPitSowedIndex, new NormalPit(1, playerOne)); // last pit sowed
        SowReducer.Result sowResult = new SowReducer.Result(
                playerOne,
                new Board(pits),
                lastPitSowedIndex,
                true,
               false
        );

        CaptureReducer.Result result = new CaptureReducer().reduce(sowResult);
        System.out.println("pseeds again " + result.getBoard().getPits().get(lastPitSowedIndex).getSeeds());

        List<Pit> updatedPits = result.getBoard().getPits();
        assertEquals(0, updatedPits.get(lastPitSowedIndex).getSeeds()); // last pit sowed should be captured
        assertEquals(0, updatedPits.get(oppositePitIndex).getSeeds()); // opponent pit should be captured
        assertEquals(7, updatedPits.get(Board.PIT_INDEX_MANCALA_ONE).getSeeds()); // captured seeds go in mancala
        assertEquals(0, updatedPits.get(Board.PIT_INDEX_MANCALA_TWO).getSeeds());
    }

    @Test
    void reduce_capturesSeedsForPlayerTwo() {
        int lastPitSowedIndex = 0;
        int oppositePitIndex = 12;

        List<Pit> pits = Board.getStartingBoard(playerOne, playerTwo).getPits();
        pits.set(lastPitSowedIndex, new NormalPit(1, playerOne)); // last pit sowed
        SowReducer.Result sowResult = new SowReducer.Result(
                playerTwo,
                new Board(pits),
                lastPitSowedIndex,
                true,
                false
        );

        CaptureReducer.Result result = new CaptureReducer().reduce(sowResult);
        List<Pit> updatedPits = result.getBoard().getPits();
        assertEquals(0, updatedPits.get(lastPitSowedIndex).getSeeds()); // last pit sowed should be captured
        assertEquals(0, updatedPits.get(oppositePitIndex).getSeeds()); // opponent pit should be captured
        assertEquals(7, updatedPits.get(Board.PIT_INDEX_MANCALA_TWO).getSeeds()); // captured seeds go in mancala
        assertEquals(0, updatedPits.get(Board.PIT_INDEX_MANCALA_ONE).getSeeds());
    }
}