package org.mancalamari.game;

import java.util.HashMap;

public interface Pit {
    int getSeeds();
    PitType getPitType();
    String getOwnerId();
    void sow();
}
