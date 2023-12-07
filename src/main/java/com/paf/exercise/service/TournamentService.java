package com.paf.exercise.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paf.exercise.dto.ExerciseDTO;
import com.paf.exercise.dto.PlayerDTO;
import com.paf.exercise.dto.TournamentDTO;
import com.paf.exercise.exception.NotFoundException;
import com.paf.exercise.exception.PlayerAlreadyRegisteredInTournamentException;
import com.paf.exercise.model.Player;
import com.paf.exercise.model.Tournament;
import com.paf.exercise.model.enums.Currency;
import com.paf.exercise.repository.PlayerRepository;
import com.paf.exercise.repository.TournamentRepository;

import jakarta.transaction.Transactional;

@Service
public class TournamentService {
  private static final String TOURNAMENT_NOT_FOUND = "Tournament not found";

  private final TournamentRepository tournamentRepository;
  private final PlayerRepository playerRepository;

  @Autowired
  public TournamentService(
      TournamentRepository tournamentRepository, PlayerRepository playerRepository) {
    this.tournamentRepository = tournamentRepository;
    this.playerRepository = playerRepository;
  }

  public Tournament createTournament(Tournament tournament) {
    return tournamentRepository.save(tournament);
  }

  public Tournament findTournament(int id) {
    Optional<Tournament> tournamentQuery = tournamentRepository.findById(id);
    if (tournamentQuery.isEmpty()) {
      throw new NotFoundException(TOURNAMENT_NOT_FOUND);
    }

    return tournamentQuery.get();
  }

  public List<PlayerDTO> getAllPlayersInTournament(Integer tournamentId) {
    Tournament tournament =
        tournamentRepository
            .findById(tournamentId)
            .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND));

    return tournament.getPlayers().stream().map(Player::convertToDTO).collect(Collectors.toList());
  }

  @Transactional
  public ExerciseDTO addPlayerToTournament(int playerId, int tournamentId) {
    Player player =
        playerRepository
            .findById(playerId)
            .orElseThrow(() -> new NotFoundException("Player not found"));
    Tournament tournament =
        tournamentRepository
            .findById(tournamentId)
            .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND));

    if (tournament.getPlayers().contains(player)) {
      throw new PlayerAlreadyRegisteredInTournamentException(
          "Player already registered in this tournament");
    }

    tournament.getPlayers().add(player);
    player.getTournaments().add(tournament);

    tournament = tournamentRepository.save(tournament);
    player = playerRepository.save(player);

    return new ExerciseDTO(
        tournament.getId(),
        tournament.getName(),
        tournament.getRewardAmount(),
        tournament.getRewardCurrency().toString(),
        player.getId(),
        player.getName());
  }

  @Transactional
  public void removePlayerFromTournament(int playerId, int tournamentId) {
    Player player =
        playerRepository
            .findById(playerId)
            .orElseThrow(() -> new NotFoundException("Player not found"));
    Tournament tournament =
        tournamentRepository
            .findById(tournamentId)
            .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND));

    if (!tournament.getPlayers().contains(player)) {
      throw new NotFoundException("Player not registered in the tournament");
    }

    tournament.getPlayers().remove(player);
    player.getTournaments().remove(tournament);
    tournamentRepository.save(tournament);
    playerRepository.save(player);
  }

  public void deleteTournament(int id) {
    tournamentRepository.deleteById(id);
  }

  public List<Tournament> findEmptyTournaments() {
    return tournamentRepository.findTournamentsWithoutPlayers();
  }

  public List<Tournament> findNonEmptyTournaments() {
    return tournamentRepository.findTournamentsWithPlayers();
  }

  public List<Tournament> findAllTournaments() {
    return tournamentRepository.findAll();
  }

  public TournamentDTO updateTournamentDetails(Integer id, TournamentDTO request) {
    Tournament tournament =
        tournamentRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND));
    tournament.setName(request.getName());
    tournament.setRewardAmount(request.getRewardAmount());
    tournament.setRewardCurrency(Currency.valueOf(request.getRewardCurrency()));

    return tournamentRepository.save(tournament).convertToDTO();
  }
}
