package org.mancalamari.request;

import org.junit.jupiter.api.Test;
import org.mancalamari.game.Board;
import org.mancalamari.game.GameState;
import org.mancalamari.game.NormalPit;
import org.mancalamari.game.Pit;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IllegalMoveValidatorTest {
    @Test
    void validate_throwsErrorIfIndexInvalid() {
        String player = "foo";
        GameState gameState = GameState.newGame(player, "bar");

        assertThrows(IllegalMoveException.class, () -> {
            int chosenPit = -1;
            SowRequest request = new SowRequest("baz", player, chosenPit, "qux");
            IllegalMoveValidator.validate(gameState, request);
        });
        assertThrows(IllegalMoveException.class, () -> {
            int chosenPit = 50;
            SowRequest request = new SowRequest("baz", player, chosenPit, "qux");
            IllegalMoveValidator.validate(gameState, request);
        });
    }

    @Test
    void validate_throwsErrorIfChosenPitBelongsToOtherPlayer() {
        String player = "foo";
        GameState gameState = GameState.newGame(player, "bar");
        int chosenPit = Board.PIT_INDEX_MANCALA_ONE + 1;
        SowRequest request = new SowRequest("baz", player, chosenPit, "qux");

        assertThrows(IllegalMoveException.class, () -> IllegalMoveValidator.validate(gameState, request));
    }

    @Test
    void validate_throwsErrorIfChosenPitIsMancala() {
        String player = "foo";
        GameState gameState = GameState.newGame(player, "bar");
        int chosenPit = Board.PIT_INDEX_MANCALA_ONE;
        SowRequest request = new SowRequest("baz", player, chosenPit, "qux");

        assertThrows(IllegalMoveException.class, () -> IllegalMoveValidator.validate(gameState, request));
    }

    @Test
    void validate_throwsErrorIfChosenPitIsEmpty() {
        String player = "foo";
        int chosenPit = 0;

        List<Pit> initialPits = Board.getStartingBoard(player, "bar").getPits();
        initialPits.set(chosenPit, new NormalPit(0, player)); // empty pit

        Board board = new Board(initialPits);
        GameState gameState = new GameState(board, Arrays.asList(player, "bar"), player, null);
        SowRequest request = new SowRequest("baz", player, chosenPit, "qux");

        assertThrows(IllegalMoveException.class, () -> IllegalMoveValidator.validate(gameState, request));
    }

    @Test
    void validate_throwsErrorIfNotThePlayersTurn() {
        String activePlayer = "foo";
        String nonActivePlayer = "bar";
        int chosenPit = 0;

        GameState gameState = GameState.newGame(activePlayer, nonActivePlayer);
        SowRequest request = new SowRequest("baz", nonActivePlayer, chosenPit, "qux");

        assertThrows(IllegalMoveException.class, () -> IllegalMoveValidator.validate(gameState, request));
    }
}