package com.paf.exercise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paf.exercise.model.TournamentPlayer;
import com.paf.exercise.model.TournamentPlayerId;

public interface TournamentPlayerRepository
    extends JpaRepository<TournamentPlayer, TournamentPlayerId> {

  Optional<TournamentPlayer> findByPlayerIdAndTournamentId(Integer playerId, Integer tournamentId);
}
