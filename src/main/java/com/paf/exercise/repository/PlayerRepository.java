package com.paf.exercise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paf.exercise.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
  Optional<Player> findById(Integer id);

  List<Player> findAll();

  void deleteById(Integer id);
}
