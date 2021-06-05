package org.mancalamari.request;

import org.mancalamari.game.GameState;
import org.mancalamari.game.Pit;
import org.mancalamari.game.PitType;

import java.util.List;

/**
 * Validates a sow request and throws exceptions if it is an illegal move.
 */
public class IllegalMoveValidator {
    public static void validate(GameState gameState, SowRequest request) throws IllegalMoveException {
        int pitIndex = request.getPitId();
        final List<Pit> pits = gameState.getBoard().getPits();
        final String currentPlayer = gameState.getCurrentPlayer();

        if(!currentPlayer.equals(request.getPlayerId())) {
            throw new IllegalMoveException("It is not your turn. The active player is: " + currentPlayer);
        }

        if(pitIndex >= pits.size() || pitIndex < 0) {
            throw new IllegalMoveException("Chosen pit does not exist: " + pitIndex);
        }

        if(!currentPlayer.equals(pits.get(pitIndex).getOwnerId())) {
            throw new IllegalMoveException("Cannot sow from another player's pit.");
        }

        if(PitType.Mancala == pits.get(pitIndex).getPitType()) {
            throw new IllegalMoveException("Cannot sow from the Mancala.");
        }

        if(pits.get(pitIndex).getSeeds() == 0) {
            throw new IllegalMoveException("Cannot sow from an empty pit.");
        }
    }
}
