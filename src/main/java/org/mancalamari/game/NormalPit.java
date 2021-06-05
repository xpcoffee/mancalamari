package org.mancalamari.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class NormalPit implements  Pit {
    @JsonProperty("pitType")
    PitType pitType = PitType.Pit;

    @JsonProperty("seeds")
    int seeds;

    @JsonProperty("ownerId")
    String ownerId;

    public NormalPit(int seeds, String ownerId) {
        this.seeds = seeds;
        this.ownerId = ownerId;
    }

    // Only for use with Jackson
    public NormalPit() { }

    /**
     * Adds a new seed to the pit.
     */
    public void sow() {
        this.seeds += 1;
    }

    /**
     * Empty the pit and return the number of seeds that were in it.
     */
    public int empty() {
        int toReturn = this.seeds;
        this.seeds = 0;
        return toReturn;
    }
}
