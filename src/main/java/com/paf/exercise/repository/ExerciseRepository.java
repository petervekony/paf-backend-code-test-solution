package com.paf.exercise.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.paf.exercise.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {}
