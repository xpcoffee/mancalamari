package org.mancalamari.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MancalaTest {

    @Test
    void putSeed() {
        Mancala mancala = new Mancala(0, "foo");
        mancala.sow();
        assertEquals(1, mancala.getSeeds());
    }

    @Test
    void captureSeeds() {
        Mancala mancala = new Mancala(0, "foo");
        mancala.captureSeeds(11);
        assertEquals(11, mancala.getSeeds());
    }
}