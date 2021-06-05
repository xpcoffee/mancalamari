package org.mancalamari.persistence;

import org.junit.jupiter.api.Test;
import org.mancalamari.game.Board;
import org.mancalamari.game.GameState;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void advanceGameState_advancesGameState() throws GameState.IllegalGameStateException {
        String playerOne = "playerOne";
        String playerTwo = "playerTwo";

        GameState startingState = GameState.newGame(playerOne, playerTwo);
        Board newBoard = startingState.getBoard().copy();
        boolean playerShouldPlayAgain = false;

        GameState nextState = GameState.advanceGameState(startingState, newBoard, playerShouldPlayAgain);

        assertEquals(playerTwo, nextState.getCurrentPlayer());
        assertEquals(newBoard, nextState.getBoard());
        assertEquals(startingState.getGameId(), nextState.getGameId());
        assertNotEquals(startingState.getGameStateToken(), nextState.getGameStateToken());
    }

    @Test
    void advanceGameState_doesNotSwitchPlayerWhenFlagIsSet() throws GameState.IllegalGameStateException {
        String playerOne = "playerOne";
        String playerTwo = "playerTwo";

        GameState startingState = GameState.newGame(playerOne, playerTwo);
        Board newBoard = startingState.getBoard().copy();
        boolean playerShouldPlayAgain = true;

        GameState nextState = GameState.advanceGameState(startingState, newBoard, playerShouldPlayAgain);

        assertEquals(playerOne, nextState.getCurrentPlayer());
    }
}