package org.mancalamari.game;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.mancalamari.persistence.BoardAttributeConverter;

import javax.persistence.*;
import java.util.*;

/**
 * Models the state of the game.
 * <p>
 * These objects should not be mutated directly; rather create new instances.
 */
@Entity
@Getter
public class GameState {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private String gameId;

    /**
     * Models the game state at a specific point in time.
     * This is used to detect and reject stale actions.
     * It must be updated every time a change is made to game state.
     */
    private String gameStateToken;
    private String currentPlayer;
    private GameStatus gameStatus;
    private String winningPlayer;

    @Convert(converter = BoardAttributeConverter.class)
    @Column( length = 100000 )
    private Board board;

    @ElementCollection(targetClass=String.class)
    private List<String> players;

    public GameState(Board board, List<String> players, String currentPlayer, GameStatus gameStatus) {
        this(null, board, players, currentPlayer, gameStatus, null);
    }

    private GameState(String gameId, Board board, List<String> players, String currentPlayer, GameStatus gameStatus, String winningPlayer) {
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.board = board;
        this.gameStatus = gameStatus;
        gameStateToken = UUID.randomUUID().toString();
        this.gameId = gameId;
        this.winningPlayer = winningPlayer;
    }

    public static GameState newGame(String idPlayerOne, String idPlayerTwo) {
        Board board = Board.getStartingBoard(idPlayerOne, idPlayerTwo);
        return new GameState(
                board,
                Arrays.asList(idPlayerOne, idPlayerTwo),
                idPlayerOne,
                GameStatus.Active
        );
    }

    public static GameState advanceGameState(GameState previousState, Board board, boolean currentPlayerShouldPlayAgain) throws IllegalGameStateException {
        String newActivePlayer = currentPlayerShouldPlayAgain ? previousState.getCurrentPlayer() : getNextPlayer(previousState);

        return new GameState(
                previousState.gameId,
                board,
                new ArrayList<>(previousState.players),
                newActivePlayer,
                GameStatus.Active,
                null
        );
    }

    public static GameState endGame(GameState previousState, Board board, String winningPlayer) {
        return new GameState(
                previousState.gameId,
                board,
                new ArrayList<>(previousState.players),
                previousState.currentPlayer,
                GameStatus.End,
                winningPlayer
        );
    }

    public static String getNextPlayer(GameState previousState) throws IllegalGameStateException {
        Optional<String> maybeNextPlayer = previousState.players.stream().filter(player -> !player.equals(previousState.currentPlayer)).findFirst();

        if(maybeNextPlayer.isEmpty()) {
            throw new IllegalGameStateException("Unable to find a next player. | Previous player: " + previousState.currentPlayer + " | Players: " + String.join("," , previousState.getPlayers()));
        }
        return maybeNextPlayer.get();
    }

    // Only for use with Jackson
    public GameState() {}

    public static class IllegalGameStateException extends Exception {
        public IllegalGameStateException(String message) {
            super(message);
        }
    }

    public static class GameNotFoundException extends Exception {
        public GameNotFoundException(String gameId) {
            super("No game found with id: " + gameId);
        }
    }
}
