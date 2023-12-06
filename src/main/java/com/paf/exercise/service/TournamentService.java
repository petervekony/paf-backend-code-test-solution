package com.paf.exercise.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paf.exercise.dto.PlayerDTO;
import com.paf.exercise.dto.TournamentDTO;
import com.paf.exercise.dto.TournamentPlayerDTO;
import com.paf.exercise.exception.PlayerAlreadyRegisteredInTournamentException;
import com.paf.exercise.model.Player;
import com.paf.exercise.model.Tournament;
import com.paf.exercise.model.TournamentPlayer;
import com.paf.exercise.model.enums.Currency;
import com.paf.exercise.repository.PlayerRepository;
import com.paf.exercise.repository.TournamentPlayerRepository;
import com.paf.exercise.repository.TournamentRepository;

import javassist.NotFoundException;

@Service
public class TournamentService {
  private static final String TOURNAMENT_NOT_FOUND = "Tournament not found";

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
    // Optional<Tournament> tournamentQuery = tournamentRepository.findById(id);
    Optional<Tournament> tournamentQuery = tournamentRepository.findByIdWithPlayers(id);
    if (tournamentQuery.isEmpty()) {
      return null;
    }

    return tournamentQuery.get();
  }

  public List<PlayerDTO> getAllPlayersInTournament(Integer tournamentId) throws NotFoundException {
    Tournament tournament =
        tournamentRepository
            .findById(tournamentId)
            .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND));

    return tournament.getPlayers().stream().map(Player::convertToDTO).collect(Collectors.toList());
  }

  @Transactional
  public TournamentPlayerDTO addPlayerToTournament(int playerId, int tournamentId)
      throws NotFoundException, PlayerAlreadyRegisteredInTournamentException {
    Player player =
        playerRepository
            .findById(playerId)
            .orElseThrow(() -> new NotFoundException("Player not found"));
    Tournament tournament =
        tournamentRepository
            .findById(tournamentId)
            .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND));

    if (tournamentPlayerRepository
        .findByPlayerIdAndTournamentId(playerId, tournamentId)
        .isPresent()) {
      throw new PlayerAlreadyRegisteredInTournamentException(
          "Player already registered in this tournament");
    }
    TournamentPlayer tournamentPlayer = new TournamentPlayer();
    tournamentPlayer.setPlayer(player);
    tournamentPlayer.setTournament(tournament);

    tournamentPlayerRepository.save(tournamentPlayer);

    return tournamentPlayer.convertToDTO();
  }

  @Transactional
  public void removePlayerFromTournament(int playerId, int tournamentId) throws NotFoundException {
    TournamentPlayer tournamentPlayer =
        tournamentPlayerRepository
            .findByPlayerIdAndTournamentId(playerId, tournamentId)
            .orElseThrow(() -> new NotFoundException("Player not registered in the tournament"));

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

  public TournamentDTO updateTournamentDetails(Integer id, TournamentDTO request)
      throws NotFoundException, IllegalArgumentException {
    Tournament tournament =
        tournamentRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND));
    if (request.getName() != null) {
      tournament.setName(request.getName());
    }
    if (request.getRewardAmount() != null) {
      tournament.setRewardAmount(request.getRewardAmount());
    }
    if (request.getRewardCurrency() != null) {
      tournament.setRewardCurrency(Currency.valueOf(request.getRewardCurrency()));
    }

    return tournamentRepository.save(tournament).convertToDTO();
  }
}
