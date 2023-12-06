package com.paf.exercise.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paf.exercise.dto.ExerciseDTO;
import com.paf.exercise.dto.PlayerDTO;
import com.paf.exercise.dto.TournamentDTO;
import com.paf.exercise.exception.PlayerAlreadyRegisteredInTournamentException;
import com.paf.exercise.model.Player;
import com.paf.exercise.model.Tournament;
import com.paf.exercise.model.enums.Currency;
import com.paf.exercise.service.TournamentService;

import javassist.NotFoundException;

@RestController
@RequestMapping("/api")
public class TournamentController {

  private final TournamentService tournamentService;

  @Autowired
  public TournamentController(TournamentService tournamentService) {
    this.tournamentService = tournamentService;
  }

  @GetMapping("/tournaments")
  public ResponseEntity<List<TournamentDTO>> getTournaments(
      @RequestParam(required = false) Boolean isEmpty) {
    List<Tournament> tournaments;

    if (Boolean.TRUE.equals(isEmpty)) {
      tournaments = tournamentService.findEmptyTournaments();
    } else {
      tournaments = tournamentService.findAllTournaments();
    }

    if (tournaments.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    List<TournamentDTO> response =
        tournaments.stream()
            .map(
                tournament -> {
                  List<Integer> playerIds =
                      tournament.getPlayers().stream()
                          .map(Player::getId)
                          .collect(Collectors.toList());
                  return new TournamentDTO(
                      tournament.getId(),
                      tournament.getName(),
                      tournament.getRewardAmount(),
                      tournament.getRewardCurrency().toString(),
                      playerIds);
                })
            .collect(Collectors.toList());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/tournaments/{id}")
  public ResponseEntity<TournamentDTO> getTournamentById(@PathVariable("id") Integer id) {
    Tournament tournament = tournamentService.findTournament(id);
    if (tournament == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    List<Integer> playerIds =
        tournament.getPlayers().stream().map(Player::getId).collect(Collectors.toList());
    TournamentDTO response =
        new TournamentDTO(
            tournament.getId(),
            tournament.getName(),
            tournament.getRewardAmount(),
            tournament.getRewardCurrency().toString(),
            playerIds);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/tournaments/{tournamentId}/players")
  public ResponseEntity<List<PlayerDTO>> getAllPlayersInTournament(
      @PathVariable Integer tournamentId) {
    try {
      List<PlayerDTO> players = tournamentService.getAllPlayersInTournament(tournamentId);
      return new ResponseEntity<>(players, HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/tournaments")
  public ResponseEntity<TournamentDTO> addTournament(@RequestBody TournamentDTO request) {
    Currency rewardCurrency;
    try {
      rewardCurrency = Currency.valueOf(request.getRewardCurrency());
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Tournament tournament =
        new Tournament(request.getName(), request.getRewardAmount(), rewardCurrency);
    Tournament createdTournament = tournamentService.createTournament(tournament);
    TournamentDTO response = createdTournament.convertToDTO();
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping("/tournaments/{id}")
  public ResponseEntity<TournamentDTO> updateTournamentDetails(
      @PathVariable("id") Integer id, @RequestBody TournamentDTO request) {
    try {
      TournamentDTO response = tournamentService.updateTournamentDetails(id, request);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/tournaments/{id}")
  public void deleteTournament(@PathVariable("id") Integer id) {
    tournamentService.deleteTournament(id);
  }

  @DeleteMapping("/tournaments/{tournamentId}/players/{playerId}")
  public ResponseEntity<Object> removePlayerFromTournament(
      @PathVariable Integer tournamentId, @PathVariable Integer playerId) {
    try {
      tournamentService.removePlayerFromTournament(playerId, tournamentId);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/tournaments/{tournamentId}/players/{playerId}")
  public ResponseEntity<ExerciseDTO> addPlayerToTournament(
      @PathVariable Integer tournamentId, @PathVariable Integer playerId) {
    try {
      ExerciseDTO response = tournamentService.addPlayerToTournament(playerId, tournamentId);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (PlayerAlreadyRegisteredInTournamentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}
