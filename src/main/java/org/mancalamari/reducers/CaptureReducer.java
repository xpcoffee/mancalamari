package org.mancalamari.reducers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Value;
import org.mancalamari.game.Board;
import org.mancalamari.game.NormalPit;
import org.mancalamari.game.Pit;

import java.util.List;

/**
 * Performs the captures that can happen after a sow action.
 */
public class CaptureReducer {
    public CaptureReducer.Result reduce(SowReducer.Result sowResult) {
        if(!sowResult.isLastPitEmpty()) {
            // capture condition not met; return early
            return new Result(sowResult.getBoard());
        }

        Board updatedBoard = sowResult.getBoard().copy();

        List<Pit> pits = updatedBoard.getPits();
        int playerPitIndex = sowResult.getLastPitIndex();
        int opponentPitIndex = Board.oppositePitIndex(playerPitIndex);

        int capturedPlayerSeed = ((NormalPit) pits.get(playerPitIndex)).empty();
        int capturedOpponentSeeds = ((NormalPit) pits.get(opponentPitIndex)).empty();

        updatedBoard.getPlayerMancala(sowResult.getCurrentPlayer()).captureSeeds(capturedPlayerSeed + capturedOpponentSeeds);

        return new Result(updatedBoard);
    }

    @Value
    @Getter
    public static class Result {
        Board board;
    }
}
