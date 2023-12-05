package com.paf.exercise.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paf.exercise.model.Player;
import com.paf.exercise.model.Tournament;
import com.paf.exercise.model.TournamentPlayer;
import com.paf.exercise.repository.PlayerRepository;
import com.paf.exercise.repository.TournamentPlayerRepository;
import com.paf.exercise.repository.TournamentRepository;

@Service
public class TournamentService {
  private final TournamentRepository tournamentRepository;
  private final TournamentPlayerRepository tournamentPlayerRepository;
  private final PlayerRepository playerRepository;

  @Autowired
  public TournamentService(
      TournamentRepository tournamentRepository,
      TournamentPlayerRepository tournamentPlayerRepository,
      PlayerRepository playerRepository) {
    this.tournamentRepository = tournamentRepository;
    this.tournamentPlayerRepository = tournamentPlayerRepository;
    this.playerRepository = playerRepository;
  }

  public Tournament createTournament(Tournament tournament) {
    return tournamentRepository.save(tournament);
  }

  public Tournament findTournament(int id) {
    Optional<Tournament> tournamentQuery = tournamentRepository.findById(id);
    if (tournamentQuery.isEmpty()) {
      return null;
    }

    return tournamentQuery.get();
  }

  public void addPlayerToTournament(int playerId, int tournamentId) {
    Player player =
        playerRepository
            .findById(playerId)
            .orElseThrow(() -> new RuntimeException("Player not found"));
    Tournament tournament =
        tournamentRepository
            .findById(tournamentId)
            .orElseThrow(() -> new RuntimeException("Tournament not found"));

    TournamentPlayer tournamentPlayer = new TournamentPlayer();
    tournamentPlayer.setPlayer(player);
    tournamentPlayer.setTournament(tournament);

    tournamentPlayerRepository.save(tournamentPlayer);
  }

  @Transactional
  public void removePlayerFromTournament(int playerId, int tournamentId) {
    TournamentPlayer tournamentPlayer =
        tournamentPlayerRepository
            .findByPlayerIdAndTournamentId(playerId, tournamentId)
            .orElseThrow(() -> new RuntimeException("Player not registered in the tournament"));

    tournamentPlayerRepository.delete(tournamentPlayer);
  }

  public void deleteTournament(int id) {
    tournamentRepository.deleteById(id);
  }

  public List<Tournament> findEmptyTournaments() {
    return tournamentRepository.findTournamentsWithoutPlayers();
  }

  public List<Tournament> findAllTournaments() {
    return tournamentRepository.findAll();
  }
}
