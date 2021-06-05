package org.mancalamari.reducers;

import org.junit.jupiter.api.Test;
import org.mancalamari.game.Board;
import org.mancalamari.game.NormalPit;
import org.mancalamari.game.Pit;
import org.mancalamari.request.SowRequest;
import org.mancalamari.game.GameState;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SowReducerTest {
    SowReducer sowReducer = new SowReducer();

    @Test
    void reduce_sowsSeeds() {
        String player = "foo";
        int chosenPit = 2;

        GameState gameState = GameState.newGame(player, "bar");
        SowRequest request = new SowRequest("baz", player, chosenPit, "qux");

        SowReducer.Result result = sowReducer.reduce(gameState, request);
        List<Pit> pits = result.getBoard().getPits();

        assertEquals(0, pits.get(chosenPit).getSeeds()); // chosen pit (player pit 3) must have been emptied
        assertEquals(7, pits.get(chosenPit + 1).getSeeds()); // player pit 4: one seed added
        assertEquals(7, pits.get(chosenPit + 2).getSeeds()); // player pit 5: one seed added
        assertEquals(7, pits.get(chosenPit + 3).getSeeds()); // player pit 6: one seed added
        assertEquals(1, pits.get(Board.PIT_INDEX_MANCALA_ONE).getSeeds()); // player mancala: one seed added
        assertEquals(7, pits.get(Board.PIT_INDEX_MANCALA_ONE + 1).getSeeds()); // other player pit 1: one seed added
        assertEquals(7, pits.get(Board.PIT_INDEX_MANCALA_ONE + 2).getSeeds()); // other player pit 2: one seed added
        assertEquals(6, pits.get(Board.PIT_INDEX_MANCALA_ONE + 3).getSeeds()); // other player pit 3: no seed added
    }

    @Test
    void reduce_sowsSeedsAroundTheEntireBoard() {
        String player = "foo";
        int chosenPit = 0;

        List<Pit> initialPits = Board.getStartingBoard(player, "bar").getPits();
        initialPits.set(chosenPit, new NormalPit(14, player)); // enough seeds for wrap around
        Board board = new Board(initialPits);
        GameState gameState = new GameState(board, Arrays.asList(player, "bar"), player, null);

        SowRequest request = new SowRequest("baz", player, chosenPit, "qux");
        SowReducer.Result result = sowReducer.reduce(gameState, request);

        List<Pit> pits = result.getBoard().getPits();
        assertEquals(1, pits.get(chosenPit).getSeeds()); // chosen pit (player pit 1): one seed added (wrap around)
        assertEquals(8, pits.get(chosenPit + 1).getSeeds()); // player pit 2: two seeds added (wrap around)
        assertEquals(7, pits.get(chosenPit + 2).getSeeds()); // player pit 3: one seeds added
        assertEquals(7, pits.get(chosenPit + 3).getSeeds()); // player pit 4: one seeds added
        assertEquals(7, pits.get(chosenPit + 4).getSeeds()); // player pit 5: one seeds added
        assertEquals(7, pits.get(chosenPit + 5).getSeeds()); // player pit 6: one seeds added
        assertEquals(1, pits.get(Board.PIT_INDEX_MANCALA_ONE).getSeeds()); // player mancala: one seed added
        assertEquals(7, pits.get(Board.PIT_INDEX_MANCALA_ONE + 1).getSeeds()); // other player pit 1: one stone added
        assertEquals(7, pits.get(Board.PIT_INDEX_MANCALA_ONE + 2).getSeeds()); // other player pit 2: one stone added
        assertEquals(7, pits.get(Board.PIT_INDEX_MANCALA_ONE + 3).getSeeds()); // other player pit 3: one stone added
        assertEquals(7, pits.get(Board.PIT_INDEX_MANCALA_ONE + 4).getSeeds()); // other player pit 4: one stone added
        assertEquals(7, pits.get(Board.PIT_INDEX_MANCALA_ONE + 5).getSeeds()); // other player pit 5: one stone added
        assertEquals(7, pits.get(Board.PIT_INDEX_MANCALA_ONE + 6).getSeeds()); // other player pit 6: one stone added
        assertEquals(0, pits.get(Board.PIT_INDEX_MANCALA_TWO).getSeeds()); // other player mancala: no seed added
    }

    @Test
    void reduce_returnsFlagIfLastPitEmpty() {
        String player = "foo";
        int chosenPit = 0;
        int seedsToSow = 2;
        int lastPit = chosenPit + seedsToSow;

        List<Pit> initialPits = Board.getStartingBoard(player, "bar").getPits();
        initialPits.set(chosenPit, new NormalPit(seedsToSow, player));
        initialPits.set(lastPit, new NormalPit(0, player)); // final pit is empty
        Board board = new Board(initialPits);
        GameState gameState = new GameState(board, Arrays.asList(player, "bar"), player, null);

        SowRequest request = new SowRequest("baz", player, chosenPit, "qux");
        SowReducer.Result result = sowReducer.reduce(gameState, request);

        assertTrue(result.isLastPitEmpty());
        assertEquals(lastPit, result.getLastPitIndex());
    }

    @Test
    void reduce_returnsFlagIfLastPitPlayerMancala() {
        String player = "foo";
        int chosenPit = 0;

        GameState gameState = GameState.newGame(player, "bar");
        SowRequest request = new SowRequest("baz", player, chosenPit, "qux");

        SowReducer.Result result = sowReducer.reduce(gameState, request);

        assertTrue(result.isLastPitPlayerMancala());
        assertFalse(result.isLastPitEmpty()); // if last pit is Mancala, we mustn't set the last pit empty flag
    }

}