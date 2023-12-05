package com.paf.exercise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paf.exercise.model.Tournament;
import com.paf.exercise.service.TournamentService;

@RestController
@RequestMapping("/exercise/tournaments")
public class TournamentController {

  private final TournamentService tournamentService;

  @Autowired
  public TournamentController(TournamentService tournamentService) {
    this.tournamentService = tournamentService;
  }

  @GetMapping("/")
  public ResponseEntity<Object> getTournament(@RequestParam(required = false) Boolean isEmpty) {
    if (Boolean.TRUE.equals(isEmpty)) {
      List<Tournament> tournaments = tournamentService.findEmptyTournaments();
      if (tournaments.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(tournaments, HttpStatus.NO_CONTENT);
    }

    List<Tournament> tournaments = tournamentService.findAllTournaments();
    if (tournaments.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(tournaments, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tournament> getTournamentById(@PathVariable("id") int id) {
    Tournament tournament = tournamentService.findTournament(id);
    if (tournament == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(tournament, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Tournament> addTournament(@RequestBody Tournament tournament) {
    Tournament createdTournament = tournamentService.createTournament(tournament);
    return new ResponseEntity<>(createdTournament, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public void deleteTournament(@PathVariable("id") Integer id) {
    tournamentService.deleteTournament(id);
  }

  @DeleteMapping("/{tournamentId}/players/{playerId}")
  public ResponseEntity<Object> removePlayerFromTournament(
      @PathVariable int tournamentId, @PathVariable int playerId) {
    tournamentService.removePlayerFromTournament(playerId, tournamentId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
