package org.mancalamari.reducers;

import org.junit.jupiter.api.Test;
import org.mancalamari.game.Board;
import org.mancalamari.game.NormalPit;

import static org.junit.jupiter.api.Assertions.*;

class EndGameConditionReducerTest {
    String playerOne = "playerOne";
    String playerTwo = "playerTwo";

    @Test
    void reduce_whenEndGame_performsEndGameCaptures_forPlayerOne() {
        Board board = Board.getStartingBoard(playerOne, playerTwo);
        // set player 2 pits to empty
        for (int i = Board.PIT_INDEX_MANCALA_ONE + 1; i < Board.PIT_INDEX_MANCALA_TWO; i++) {
            board.getPits().set(i, new NormalPit(0, playerTwo));
        }

        EndGameConditionReducer.Result result = new EndGameConditionReducer().reduce(new CaptureReducer.Result(board));
        Board resultBoard = result.getBoard();

        // player 2 pits must have been captured
        for (int i = 0; i < Board.PIT_INDEX_MANCALA_ONE; i++) {
            assertEquals(0, resultBoard.getPit(i).getSeeds());
        }

        assertEquals(36, resultBoard.getPit(Board.PIT_INDEX_MANCALA_ONE).getSeeds());
        assertEquals(0, resultBoard.getPit(Board.PIT_INDEX_MANCALA_TWO).getSeeds());
        assertTrue(result.getWinningPlayer().isPresent());
        assertEquals(playerOne, result.getWinningPlayer().get());
    }

    @Test
    void reduce_whenEndGame_performsEndGameCaptures_forPlayerTwo() {
        Board board = Board.getStartingBoard(playerOne, playerTwo);
        // set player 1 pits to empty
        for (int i = 0; i < Board.PIT_INDEX_MANCALA_ONE; i++) {
            board.getPits().set(i, new NormalPit(0, playerOne));
        }

        EndGameConditionReducer.Result result = new EndGameConditionReducer().reduce(new CaptureReducer.Result(board));
        Board resultBoard = result.getBoard();

        // player 2 pits must have been captured
        for (int i = Board.PIT_INDEX_MANCALA_ONE + 1; i < Board.PIT_INDEX_MANCALA_TWO; i++) {
            assertEquals(0, resultBoard.getPit(i).getSeeds());
        }

        assertEquals(0, resultBoard.getPit(Board.PIT_INDEX_MANCALA_ONE).getSeeds());
        assertEquals(36, resultBoard.getPit(Board.PIT_INDEX_MANCALA_TWO).getSeeds());
        assertTrue(result.getWinningPlayer().isPresent());
        assertEquals(playerTwo, result.getWinningPlayer().get());
    }

    @Test
    void reduce_whenNotEndGame_doesNotAffectTheBoard() {
        Board board = Board.getStartingBoard(playerOne, playerTwo);
        EndGameConditionReducer.Result result = new EndGameConditionReducer().reduce(new CaptureReducer.Result(board));
        Board resultBoard = result.getBoard();

        for (int i = 0; i < Board.NUMBER_OF_PITS; i++) {
            assertEquals(board.getPit(i).getSeeds(), resultBoard.getPit(i).getSeeds());
        }
    }
}