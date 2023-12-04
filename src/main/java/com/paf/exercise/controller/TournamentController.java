package com.paf.exercise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paf.exercise.model.Tournament;
import com.paf.exercise.service.TournamentService;

@RestController
@RequestMapping("/exercise/tournament")
public class TournamentController {

  private final TournamentService tournamentService;

  @Autowired
  public TournamentController(TournamentService tournamentService) {
    this.tournamentService = tournamentService;
  }

  @GetMapping("/")
  public List<Tournament> getTournament(@RequestParam(required = false) boolean isEmpty) {
    if (isEmpty) {
      return tournamentService.findEmptyTournaments();
    }

    return tournamentService.findAllTournaments();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tournament> getTournamentById(@PathVariable("id") int id) {
    Tournament tournament = tournamentService.findTournament(id);
    if (tournament == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(tournament, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public void deleteTournament(@PathVariable("id") Integer id) {
    tournamentService.deleteTournament(id);
  }
}
