package org.mancalamari.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void copy_clonesPits() {
        Board board = Board.getStartingBoard("foo", "bar");
        List<Pit> first = board.copy().getPits();
        List<Pit> second = board.copy().getPits();
        assertNotEquals(first, second); // different objects
    }

    @Test
    void getStartingBoard() {
        String playerOne = "foo";
        String playerTwo = "bar";
        Board board = Board.getStartingBoard("foo", "bar");
        List<Pit> pits = board.getPits();


        assertEquals(Board.NUMBER_OF_PITS, pits.size());

        assertEquals(0, pits.get(Board.PIT_INDEX_MANCALA_ONE).getSeeds());
        assertEquals(PitType.Mancala, pits.get(Board.PIT_INDEX_MANCALA_ONE).getPitType());

        for (int i = 0; i < Board.PIT_INDEX_MANCALA_ONE; i++) {
            assertEquals(Board.STARTING_PIT_SEEDS, pits.get(i).getSeeds());
            assertEquals(PitType.Pit, pits.get(i).getPitType());
            assertEquals(playerOne, pits.get(i).getOwnerId());
        }

        assertEquals(0, pits.get(Board.PIT_INDEX_MANCALA_TWO).getSeeds());
        assertEquals(PitType.Mancala, pits.get(Board.PIT_INDEX_MANCALA_TWO).getPitType());

        for (int i = Board.PIT_INDEX_MANCALA_ONE + 1; i < Board.PIT_INDEX_MANCALA_TWO; i++) {
            assertEquals(Board.STARTING_PIT_SEEDS, pits.get(i).getSeeds());
            assertEquals(PitType.Pit, pits.get(i).getPitType());
            assertEquals(playerTwo, pits.get(i).getOwnerId());
        }
    }

    @Test
    void nextPitIndex() {
        assertEquals(3, Board.nextPitIndex(2));

        // FIXME: switch to providing an iterator; handle pit index internally
        // it's possible to get a weird state if the caller passes in a number larger than the NUMBER_OF_PITS
        assertEquals(0, Board.nextPitIndex(Board.NUMBER_OF_PITS));
    }


    @Test
    void oppositePitIndex() {
        assertEquals(12, Board.oppositePitIndex(0));
        assertEquals(2, Board.oppositePitIndex(10));
        assertEquals(8, Board.oppositePitIndex(4));
    }
}