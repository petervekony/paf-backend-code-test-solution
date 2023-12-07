package com.paf.exercise.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.paf.exercise.controller.PlayerController;
import com.paf.exercise.dto.PlayerDTO;
import com.paf.exercise.model.Player;
import com.paf.exercise.service.PlayerService;

@SpringBootTest
class PlayerControllerTests {
  private static final Integer PLAYER_ID = 1;
  private static final String PLAYER_NAME = "John Doe";
  private static final String UPDATED_NAME = "Jane Doe";

  @Mock private PlayerService playerService;

  private PlayerController playerController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    playerController = new PlayerController(playerService);
  }

  @Test
  void getAllPlayers_WhenPlayersExist_ShouldReturnPlayers() {
    List<Player> players = List.of(new Player(PLAYER_NAME));
    when(playerService.getAllPlayers()).thenReturn(players);

    ResponseEntity<List<PlayerDTO>> response = playerController.getAllPlayers();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertFalse(response.getBody().isEmpty());
    assertEquals(PLAYER_NAME, response.getBody().get(0).getName());
  }

  @Test
  void getAllPlayers_WhenNoPlayersExist_ShouldReturnNoContent() {
    when(playerService.getAllPlayers()).thenReturn(Collections.emptyList());

    ResponseEntity<List<PlayerDTO>> response = playerController.getAllPlayers();

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void createPlayer_ShouldCreatePlayer() {
    PlayerDTO playerDTO = new PlayerDTO();
    playerDTO.setName(PLAYER_NAME);
    Player mockPlayer = new Player(PLAYER_NAME);
    when(playerService.createPlayer(anyString())).thenReturn(mockPlayer);

    ResponseEntity<PlayerDTO> response = playerController.createPlayer(playerDTO);

    assertNotNull(response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(PLAYER_NAME, response.getBody().getName());
  }

  @Test
  void deletePlayer_ShouldDeletePlayer() {
    ResponseEntity<HttpStatus> response = playerController.deletePlayer(PLAYER_ID);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(playerService, times(1)).deletePlayer(PLAYER_ID);
  }

  @Test
  void updatePlayer_ShouldUpdatePlayer() {
    PlayerDTO playerDTO = new PlayerDTO();
    playerDTO.setName(UPDATED_NAME);
    Player updatedPlayer = new Player(UPDATED_NAME);
    when(playerService.updatePlayer(eq(PLAYER_ID), any(PlayerDTO.class))).thenReturn(updatedPlayer);

    ResponseEntity<PlayerDTO> response = playerController.updatePlayer(PLAYER_ID, playerDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(UPDATED_NAME, response.getBody().getName());
  }

  @Test
  void getPlayer_WhenPlayerExists_ShouldReturnPlayer() {
    Player mockPlayer = new Player(PLAYER_NAME);
    when(playerService.getPlayer(PLAYER_ID)).thenReturn(mockPlayer);

    ResponseEntity<PlayerDTO> response = playerController.getPlayer(PLAYER_ID);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(PLAYER_NAME, response.getBody().getName());
  }
}
