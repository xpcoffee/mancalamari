package org.mancalamari;

import lombok.extern.log4j.Log4j2;
import org.mancalamari.reducers.CaptureReducer;
import org.mancalamari.reducers.EndGameConditionReducer;
import org.mancalamari.request.*;
import org.mancalamari.game.GameState;
import org.mancalamari.persistence.GameStateCrudRepository;
import org.mancalamari.reducers.SowReducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RestController
public class HttpController {
    @Autowired
    GameStateCrudRepository repository;

    /**
     * Start a new game.
     */
    @PutMapping("/game")
    public GameState play() {
        return repository.save(GameState.newGame("Ackfoo", "Ackbar"));
    }

    /**
     * Get the state of the game
     */
    @GetMapping("/game")
    public GameState get(@Valid @RequestBody GetGameRequest request) throws GameState.GameNotFoundException {
        return getGameState(request);
    }

    /**
     * Affect the game.
     */
    @PostMapping("/game")
    public GameState sow(@Valid @RequestBody SowRequest request) throws GameState.IllegalGameStateException, GameState.GameNotFoundException, IllegalMoveException {
        GameState gameState = getGameState(request);

        MutatingRequestValidator.validate(gameState, request);
        IllegalMoveValidator.validate(gameState, request);

        SowReducer.Result sowResult = new SowReducer().reduce(gameState, request);
        boolean currentPlayerShouldPlayAgain = sowResult.isLastPitPlayerMancala();
        CaptureReducer.Result captureResult = new CaptureReducer().reduce(sowResult);
        EndGameConditionReducer.Result endGameResult = new EndGameConditionReducer().reduce(captureResult);

        GameState newGameState = endGameResult.getWinningPlayer().isPresent()
                ? GameState.endGame(gameState, endGameResult.getBoard(), endGameResult.getWinningPlayer().get())
                : GameState.advanceGameState(gameState, captureResult.getBoard(), currentPlayerShouldPlayAgain);

        return repository.save(newGameState);
    }

    private GameState getGameState(Request request) throws GameState.GameNotFoundException {
        String gameId = request.getGameId();
        Optional<GameState> maybeGameState = repository.findById(gameId);

        if (maybeGameState.isEmpty()) {
            log.info("Game with ID " + gameId + " not found.");
            throw new GameState.GameNotFoundException(gameId);
        }

        return maybeGameState.get();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(GameState.IllegalGameStateException.class)
    public String handleIllegalGameStateExceptions(IllegalMoveException ex) {
        return "The application encountered an error. Please try again in a while.";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(GameState.GameNotFoundException.class)
    public String handleGameNotFoundException(GameState.GameNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalMoveException.class)
    public String handleIllegalMoveExceptions(IllegalMoveException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
