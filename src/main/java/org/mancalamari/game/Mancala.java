package org.mancalamari.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Mancala implements Pit {
    /**
     * The type of pit
     */
    @JsonProperty("pitType")
    PitType pitType = PitType.Mancala;
    /**
     * The number of seeds in the pit
     */
    @JsonProperty("seeds")
    int seeds;
    /**
     * The owner of the pit
     */
    @JsonProperty("ownerId")
    String ownerId;

    public Mancala(int seeds, String ownerId) {
        this.seeds = seeds;
        this.ownerId = ownerId;
    }

    // Only for use with Jackson
    public Mancala() { }

    /**
     * Adds a seed to the Mancala.
     */
    public void sow() {
        this.seeds += 1;
    }

    public void captureSeeds(int capturedSeeds) {
        this.seeds += capturedSeeds;
    }
}
