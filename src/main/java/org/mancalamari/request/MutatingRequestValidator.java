package org.mancalamari.request;

import org.mancalamari.game.GameState;
import org.mancalamari.game.GameStatus;
import org.mancalamari.game.Pit;
import org.mancalamari.game.PitType;

import java.util.List;

/**
 * Validates a mutating request is allowed by the current game state.
 */
public class MutatingRequestValidator {
    public static void validate(GameState gameState, MutatingRequest request) throws IllegalMoveException {
        if(GameStatus.Active != gameState.getGameStatus()) {
            throw new IllegalMoveException("The game is no longer active. Please start a new game.");
        }

        if(!gameState.getGameStateToken().equals(request.getGameStateToken())) {
            throw new IllegalMoveException("The current request is stale. Please refresh the game and try again.");
        }
    }
}
