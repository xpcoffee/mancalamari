package org.mancalamari.request;

public interface Request {
    /**
     * The token representing the game at a specific point in time.
     * Used to detect if an action is stale so that it can be discarded.
     */
    String getGameId();
}
