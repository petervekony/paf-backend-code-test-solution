package com.paf.exercise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paf.exercise.model.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
  Optional<Tournament> findById(Integer id);

  List<Tournament> findTournamentsWithoutPlayers();

  List<Tournament> findAll();

  void deleteById(int id);
}
