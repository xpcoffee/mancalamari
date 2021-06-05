package org.mancalamari.reducers;

import lombok.Getter;
import lombok.Value;
import org.mancalamari.game.Board;
import org.mancalamari.game.Mancala;
import org.mancalamari.game.NormalPit;
import org.mancalamari.game.Pit;

import java.util.Optional;

/**
 * Determines if the win condition is met; performs final captures if it is.
 */
public class EndGameConditionReducer {
    public EndGameConditionReducer.Result reduce(CaptureReducer.Result captureReducerResult) {
        Board updatedBoard = captureReducerResult.getBoard().copy();

        int playerOneRowSeeds = tallyPlayerOneRowSeeds(updatedBoard);
        int playerTwoRowSeeds = tallyPlayerTwoRowSeeds(updatedBoard);

        if(playerOneRowSeeds == 0) {
            // end game condition. player 1 row empty. player 2 captures their remaining seeds
            int remainingPlayerTwoSeeds = 0;
            for (int i = Board.PIT_INDEX_MANCALA_ONE + 1; i < Board.PIT_INDEX_MANCALA_TWO; i++) {
                remainingPlayerTwoSeeds += ((NormalPit) updatedBoard.getPit(i)).empty();
            }
            ((Mancala) updatedBoard.getPit(Board.PIT_INDEX_MANCALA_TWO)).captureSeeds(remainingPlayerTwoSeeds);

            String winner = determineWinningPlayer(updatedBoard);
            return new Result(updatedBoard, Optional.of(winner));
        }

        if(playerTwoRowSeeds == 0) {
            // end game condition. player 2 row empty. player 1 captures their remaining seeds
            int remainingPlayerOneRowSeeds = 0;
            for (int i = 0; i < Board.PIT_INDEX_MANCALA_ONE; i++) {
                remainingPlayerOneRowSeeds += ((NormalPit) updatedBoard.getPit(i)).empty();
            }
            ((Mancala) updatedBoard.getPit(Board.PIT_INDEX_MANCALA_ONE)).captureSeeds(remainingPlayerOneRowSeeds);

            String winner = determineWinningPlayer(updatedBoard);
            return new Result(updatedBoard, Optional.of(winner));
        }

        return new Result(updatedBoard, Optional.empty());
    }

    private int tallyPlayerOneRowSeeds(Board board) {
        int tally = 0;
        for (int i = 0; i < Board.PIT_INDEX_MANCALA_ONE; i++) {
            tally += board.getPit(i).getSeeds();
        }
        return tally;
    }

    private int tallyPlayerTwoRowSeeds(Board board) {
        int tally = 0;
        for (int i = Board.PIT_INDEX_MANCALA_ONE + 1; i < Board.PIT_INDEX_MANCALA_TWO; i++) {
            tally += board.getPit(i).getSeeds();
        }
        return tally;
    }

    private String determineWinningPlayer(Board board) {
        Pit playerOneMancala = board.getPit(Board.PIT_INDEX_MANCALA_ONE);
        Pit playerTwoMancala = board.getPit(Board.PIT_INDEX_MANCALA_TWO);
        return playerOneMancala.getSeeds() > playerTwoMancala.getSeeds() ? playerOneMancala.getOwnerId() : playerTwoMancala.getOwnerId();
    }

    @Value
    @Getter
    public static class Result {
        Board board;
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        Optional<String> winningPlayer;
    }
}
