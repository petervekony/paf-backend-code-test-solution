package com.paf.exercise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.paf.exercise.model.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
  Optional<Tournament> findById(Integer id);

  @Query(
      "SELECT t FROM Tournament t WHERE t.id NOT IN (SELECT tp.tournament.id FROM TournamentPlayer"
          + " tp)")
  List<Tournament> findTournamentsWithoutPlayers();

  @Query("SELECT t FROM Tournament t LEFT JOIN FETCH t.players WHERE t.id = :id")
  Optional<Tournament> findByIdWithPlayers(@Param("id") Integer id);

  List<Tournament> findAll();

  void deleteById(int id);
}
