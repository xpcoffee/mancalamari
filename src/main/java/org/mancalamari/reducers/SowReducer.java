package org.mancalamari.reducers;

import lombok.Getter;
import lombok.Value;
import org.mancalamari.request.SowRequest;
import org.mancalamari.game.*;
import org.mancalamari.game.GameState;

public class SowReducer {

    public SowReducer.Result reduce(GameState gameState, SowRequest request) {
        int startingPitIndex = request.getPitId();
        Board updatedBoard = gameState.getBoard().copy();

        final String player = request.getPlayerId();

        NormalPit startingPit = (NormalPit) updatedBoard.getPit(startingPitIndex);
        int seedsToSow = startingPit.empty();

        boolean lastPitEmpty = false;
        boolean lastPitPlayerMancala = false;
        int lastPitIndex = 0;

        int pitIndex = startingPitIndex;
        while(seedsToSow > 0){
            pitIndex = Board.nextPitIndex(pitIndex);
            Pit pit = updatedBoard.getPit(pitIndex);

            if(PitType.Mancala == pit.getPitType() && !player.equals(pit.getOwnerId())) {
                continue;
            }

            if(seedsToSow == 1) {
                if(PitType.Mancala == pit.getPitType() && player.equals(pit.getOwnerId())) {
                    lastPitPlayerMancala = true;
                } else if(pit.getSeeds() == 0) {
                    lastPitEmpty = true;
                    lastPitIndex = pitIndex;
                }
            }

            pit.sow();
            seedsToSow--;
        }
        return new Result(player, updatedBoard, lastPitIndex, lastPitEmpty, lastPitPlayerMancala);
    }

    @Value
    @Getter
    public static class Result {
        String currentPlayer;
        Board board;
        int lastPitIndex;
        boolean lastPitEmpty;
        boolean lastPitPlayerMancala;
    }
}
