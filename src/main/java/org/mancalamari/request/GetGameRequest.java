package org.mancalamari.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * Models an action that returns the current state of the game.
 */
@AllArgsConstructor
@Getter
public class GetGameRequest implements Request {
    public GetGameRequest() {}

    /**
     * The ID of the game being played.
     */
    @NotBlank(message = "gameId ID is required")
    String gameId;
}

