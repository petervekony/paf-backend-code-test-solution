package com.paf.exercise.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.paf.exercise.dto.PlayerDTO;
import com.paf.exercise.exception.NotFoundException;
import com.paf.exercise.model.Player;
import com.paf.exercise.repository.PlayerRepository;

@SpringBootTest
class PlayerServiceTests {

  @Mock private PlayerRepository playerRepository;

  @InjectMocks private PlayerService playerService;

  @BeforeEach
  void setUp() {
    playerService = new PlayerService(playerRepository);
  }

  @Test
  void testCreatePlayer() {
    String playerName = "John Doe";
    Player mockPlayer = new Player(playerName);
    when(playerRepository.save(any(Player.class))).thenReturn(mockPlayer);

    Player createdPlayer = playerService.createPlayer(playerName);

    assertNotNull(createdPlayer);
    assertEquals(playerName, createdPlayer.getName());
  }

  @Test
  void testGetPlayerSuccess() {
    Integer playerId = 1;
    Player mockPlayer = new Player("John Doe");
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(mockPlayer));

    Player foundPlayer = playerService.getPlayer(playerId);

    assertNotNull(foundPlayer);
    assertEquals("John Doe", foundPlayer.getName());
  }

  @Test
  void testGetPlayerNotFound() {
    Integer playerId = 1;
    when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> {
          playerService.getPlayer(playerId);
        });
  }

  @Test
  void testGetAllPlayers() {
    List<Player> mockPlayers = Arrays.asList(new Player("John Doe"), new Player("Jane Doe"));
    when(playerRepository.findAll()).thenReturn(mockPlayers);

    List<Player> players = playerService.getAllPlayers();

    assertNotNull(players);
    assertEquals(2, players.size());
  }

  @Test
  void testDeletePlayer() {
    Integer playerId = 1;
    doNothing().when(playerRepository).deleteById(playerId);

    assertDoesNotThrow(() -> playerService.deletePlayer(playerId));
  }

  @Test
  void testUpdatePlayerSuccess() {
    Integer playerId = 1;
    String updatedName = "Jane Doe";
    Player mockPlayer = new Player("John Doe");
    PlayerDTO playerDTO = new PlayerDTO();
    playerDTO.setName(updatedName);

    when(playerRepository.findById(playerId)).thenReturn(Optional.of(mockPlayer));
    when(playerRepository.save(any(Player.class))).thenAnswer(i -> i.getArgument(0, Player.class));

    Player updatedPlayer = playerService.updatePlayer(playerId, playerDTO);

    assertNotNull(updatedPlayer);
    assertEquals(updatedName, updatedPlayer.getName());
  }

  @Test
  void testUpdatePlayerNotFound() {
    Integer playerId = 1;
    PlayerDTO playerDTO = new PlayerDTO();
    playerDTO.setName("Jane Doe");

    when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> {
          playerService.updatePlayer(playerId, playerDTO);
        });
  }

  @Test
  void testDeleteNonExistentPlayer() {
    Integer playerId = 99;
    doNothing().when(playerRepository).deleteById(playerId);
    assertDoesNotThrow(() -> playerService.deletePlayer(playerId));
    verify(playerRepository, times(1)).deleteById(playerId);
  }
}
