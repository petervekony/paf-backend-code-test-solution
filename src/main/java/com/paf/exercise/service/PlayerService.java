package com.paf.exercise.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paf.exercise.dto.PlayerDTO;
import com.paf.exercise.exception.NotFoundException;
import com.paf.exercise.model.Player;
import com.paf.exercise.repository.PlayerRepository;

@Service
public class PlayerService {
  private final PlayerRepository playerRepository;

  @Autowired
  public PlayerService(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  public Player createPlayer(String name) {
    return playerRepository.save(new Player(name));
  }

  public Player getPlayer(Integer id) {
    return playerRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Player not found"));
  }

  public List<Player> getAllPlayers() {
    return playerRepository.findAll();
  }

  public void deletePlayer(Integer id) {
    playerRepository.deleteById(id);
  }

  public Player updatePlayer(Integer id, PlayerDTO playerDetails) {
    Player player =
        playerRepository.findById(id).orElseThrow(() -> new NotFoundException("Player not found"));

    player.setName(playerDetails.getName());
    return playerRepository.save(player);
  }
}
