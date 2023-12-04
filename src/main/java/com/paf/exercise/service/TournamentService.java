package com.paf.exercise.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paf.exercise.model.Tournament;
import com.paf.exercise.repository.TournamentRepository;

@Service
public class TournamentService {
  private final TournamentRepository tournamentRepository;

  @Autowired
  public TournamentService(TournamentRepository tournamentRepository) {
    this.tournamentRepository = tournamentRepository;
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

  public void deleteTournament(Integer id) {
    tournamentRepository.deleteById(id);
  }

  public List<Tournament> findEmptyTournaments() {
    return tournamentRepository.findTournamentsWithoutPlayers();
  }

  public List<Tournament> findAllTournaments() {
    return tournamentRepository.findAll();
  }
}
