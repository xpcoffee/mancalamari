package org.mancalamari.request;

import org.junit.jupiter.api.Test;
import org.mancalamari.game.Board;
import org.mancalamari.game.GameState;

import static org.junit.jupiter.api.Assertions.*;

class MutatingRequestValidatorTest {
    @Test
    void validate_throwsErrorIfRequestStale() {
        GameState gameState = GameState.newGame("foo", "bar");

        assertThrows(IllegalMoveException.class, () -> {
            MutatingRequest request = new MutatingRequest() {
                @Override
                public String getGameStateToken() {
                    return "baz";
                }

                @Override
                public String getGameId() {
                    return null;
                }
            };
            MutatingRequestValidator.validate(gameState, request);
        });
    }

    @Test
    void validate_throwsErrorIfGameStateIsNotActive() {
        GameState gameState = GameState.newGame("foo", "bar");
        GameState finalState = GameState.endGame(gameState, gameState.getBoard(), "foo");

        assertThrows(IllegalMoveException.class, () -> {
            MutatingRequest request = new MutatingRequest() {
                @Override
                public String getGameStateToken() {
                    return finalState.getGameStateToken();
                }

                @Override
                public String getGameId() {
                    return null;
                }
            };
            MutatingRequestValidator.validate(finalState, request);
        });
    }
}