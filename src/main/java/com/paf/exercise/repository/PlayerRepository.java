package com.paf.exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paf.exercise.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Integer> {}
