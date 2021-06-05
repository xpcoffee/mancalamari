package org.mancalamari.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Models an action that a player can take during the game.
 */
@AllArgsConstructor
@Getter
public class SowRequest implements MutatingRequest {
    @NotBlank(message = "gameStateToken is required")
    String gameStateToken;

    /**
     * The ID of the player taking the action.
     */
    @NotBlank(message = "playerId is required")
    String playerId;
    /**
     * The ID of the pit targeted by the player.
     */
    @NotNull(message = "pitId is required")
    Integer pitId;
    /**
     * The ID of the game being played.
     */
    @NotBlank(message = "gameId ID is required")
    String gameId;
}

