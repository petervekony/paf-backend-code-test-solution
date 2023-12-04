package com.paf.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paf.exercise.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
  List<Exercise> findByTournamentId(int tournamentId);
}
