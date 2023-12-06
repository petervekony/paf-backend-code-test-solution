package com.paf.exercise.repository;

import com.paf.exercise.model.Tournament;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
  Optional<Tournament> findById(Integer id);

  @Query("SELECT t FROM Tournament t WHERE t.players IS EMPTY")
  List<Tournament> findTournamentsWithoutPlayers();

  @Query("SELECT t FROM Tournament t WHERE t.players IS NOT EMPTY")
  List<Tournament> findTournamentsWithPlayers();

  List<Tournament> findAll();

  void deleteById(int id);
}
