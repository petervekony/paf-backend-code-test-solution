package com.paf.exercise.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paf.exercise.dto.PlayerDTO;
import com.paf.exercise.model.Player;
import com.paf.exercise.service.PlayerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PlayerController {
  private final PlayerService playerService;

  @Autowired
  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @GetMapping("/players")
  public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
    List<Player> allPlayers = playerService.getAllPlayers();
    if (allPlayers.isEmpty()) {
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
    List<PlayerDTO> response =
        allPlayers.stream()
            .map(player -> new PlayerDTO(player.getId(), player.getName()))
            .collect(Collectors.toList());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/players/{id}")
  public ResponseEntity<PlayerDTO> getPlayer(@PathVariable("id") Integer id) {
    Player player = playerService.getPlayer(id);
    return new ResponseEntity<>(new PlayerDTO(player.getId(), player.getName()), HttpStatus.OK);
  }

  @PostMapping("/players")
  public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerDTO playerRequest) {
    Player created = playerService.createPlayer(playerRequest.getName());
    return new ResponseEntity<>(
        new PlayerDTO(created.getId(), created.getName()), HttpStatus.CREATED);
  }

  @DeleteMapping("/players/{id}")
  public ResponseEntity<HttpStatus> deletePlayer(@PathVariable("id") Integer id) {
    playerService.deletePlayer(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/players/{id}")
  public ResponseEntity<PlayerDTO> updatePlayer(
      @PathVariable("id") Integer id, @Valid @RequestBody PlayerDTO playerDetails) {
    Player updatedPlayer = playerService.updatePlayer(id, playerDetails);
    return new ResponseEntity<>(
        new PlayerDTO(updatedPlayer.getId(), updatedPlayer.getName()), HttpStatus.OK);
  }
}
