package org.mancalamari.persistence;

import org.mancalamari.game.GameState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStateCrudRepository extends CrudRepository<GameState, String> { }
