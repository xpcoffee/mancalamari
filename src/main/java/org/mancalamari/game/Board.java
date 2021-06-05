package org.mancalamari.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.RequiredArgsConstructor;
import org.mancalamari.persistence.BoardDeserializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Models the Mancala board.
 * <p>
 * Objects of this type are intended to be immutable.
 */
@JsonDeserialize(using = BoardDeserializer.class)
@RequiredArgsConstructor
public class Board {
    /**
     * The board is modelled as an array. The order of elements
     * in the array is the order in which seeds are sown.
     */
    @JsonProperty("pits")
    final List<Pit> pits;

    /**
     * Returns a copy of the current board.
     */
    public Board copy() {
        return new Board(this.clonePits());
    }

    /**
     * Returns a clone of the pits in board state.
     */
    public List<Pit> getPits() {
        return this.pits;
    }

    /**
     * Returns a clone of the pits in board state.
     */
    private List<Pit> clonePits() {
        List<Pit> clone = new ArrayList<>();
        for (Pit pit : pits) {
            clone.add(Board.clonePit(pit));
        }
        return clone;
    }

    /**
     * Returns the Mancala pit of the given player.
     */
    public Mancala getPlayerMancala(String playerId) {
        if (playerId.equals(pits.get(PIT_INDEX_MANCALA_ONE).getOwnerId())) {
            return (Mancala) pits.get(PIT_INDEX_MANCALA_ONE);
        } else {
            return (Mancala) pits.get(PIT_INDEX_MANCALA_TWO);
        }
    }

    /**
     * Returns the pit at the given index;
     */
    public Pit getPit(int pitIndex) {
        return this.getPits().get(pitIndex);
    }

    private static Pit clonePit(Pit pit) {
        if (pit.getPitType() == PitType.Mancala) {
            return new Mancala(pit.getSeeds(), pit.getOwnerId());
        } else {
            return new NormalPit(pit.getSeeds(), pit.getOwnerId());
        }
    }

    public static int NORMAL_PITS = 6;
    public static int NUMBER_OF_PITS = (NORMAL_PITS * 2) + 2;
    public static int PIT_INDEX_MANCALA_ONE = NORMAL_PITS;
    public static int PIT_INDEX_MANCALA_TWO = NUMBER_OF_PITS - 1;
    public static int STARTING_PIT_SEEDS = 6;

    public static Board getStartingBoard(String ownerIdPlayerOne, String ownerIdPlayerTwo) {
        ArrayList<Pit> pits = new ArrayList<>();

        for (int i = 0; i < NORMAL_PITS; i++) {
            pits.add(new NormalPit(STARTING_PIT_SEEDS, ownerIdPlayerOne));
        }
        pits.add(new Mancala(0, ownerIdPlayerOne));

        for (int i = 0; i < NORMAL_PITS; i++) {
            pits.add(new NormalPit(STARTING_PIT_SEEDS, ownerIdPlayerTwo));
        }
        pits.add(new Mancala(0, ownerIdPlayerTwo));

        return new Board(pits);
    }

    /**
     * Returns the index of the next pit in order of sowing.
     */
    public static int nextPitIndex(int currentPitIndex) {
        int pitIndex = currentPitIndex + 1;
        // wrap around the board if we've reached the end
        return pitIndex < NUMBER_OF_PITS ? pitIndex : 0;
    }

    /**
     * Returns the index of the pit opposite to the given pit index.
     */
    public static int oppositePitIndex(int currentPitIndex) {
        /*
        Board structure w.r.t pit indices
        [13] 12 11 10 09 08 07      <-- player 2
             00 01 02 03 04 05 [06] <-- player 1
         */

        // Last index - mancala offset - given index
        return (NUMBER_OF_PITS - 1) - 1 - currentPitIndex;
    }
}
