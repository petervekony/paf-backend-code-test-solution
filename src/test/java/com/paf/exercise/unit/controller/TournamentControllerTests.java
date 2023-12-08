package com.paf.exercise.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.paf.exercise.controller.TournamentController;
import com.paf.exercise.dto.ExerciseDTO;
import com.paf.exercise.dto.PlayerDTO;
import com.paf.exercise.dto.TournamentDTO;
import com.paf.exercise.model.Tournament;
import com.paf.exercise.model.enums.Currency;
import com.paf.exercise.service.TournamentService;

class TournamentControllerTests {
  private static final Integer TOURNAMENT_ID = 1;
  private static final Integer PLAYER_ID = 1;
  private static final String TOURNAMENT_NAME = "Tournament";
  private static final Double REWARD_AMOUNT = 1000.00;
  private static final Currency REWARD_CURRENCY = Currency.EUR;
  private static final String PLAYER_NAME = "John Doe";

  @Mock private TournamentService tournamentService;

  private TournamentController tournamentController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    tournamentController = new TournamentController(tournamentService);
  }

  @Test
  void getAllPlayerInTournament_ShouldReturnOnePlayer() {
    PlayerDTO mockPlayer = new PlayerDTO(PLAYER_NAME);
    mockPlayer.setId(PLAYER_ID);
    List<PlayerDTO> mockPlayerList = List.of(mockPlayer);

    when(tournamentService.getAllPlayersInTournament(TOURNAMENT_ID)).thenReturn(mockPlayerList);

    ResponseEntity<List<PlayerDTO>> response =
        tournamentController.getAllPlayersInTournament(TOURNAMENT_ID);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertFalse(response.getBody().isEmpty());
    assertEquals(1, response.getBody().size());
    assertEquals(PLAYER_ID, response.getBody().get(0).getId());
  }

  @Test
  void getTournaments_WhenTournamentsExist_ShouldReturnTournaments() {
    List<Tournament> mockTournaments =
        List.of(new Tournament(TOURNAMENT_NAME, REWARD_AMOUNT, REWARD_CURRENCY));
    when(tournamentService.findAllTournaments()).thenReturn(mockTournaments);

    ResponseEntity<List<TournamentDTO>> response = tournamentController.getTournaments(null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertFalse(response.getBody().isEmpty());
    assertEquals(TOURNAMENT_NAME, response.getBody().get(0).getName());
  }

  @Test
  void getTournamentById_WhenTournamentExists_ShouldReturnTournament() {
    Tournament mockTournament = new Tournament(TOURNAMENT_NAME, REWARD_AMOUNT, REWARD_CURRENCY);
    when(tournamentService.findTournament(TOURNAMENT_ID)).thenReturn(mockTournament);

    ResponseEntity<TournamentDTO> response = tournamentController.getTournamentById(TOURNAMENT_ID);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(TOURNAMENT_NAME, response.getBody().getName());
  }

  @Test
  void addTournament_ValidTournament_ShouldCreateTournament() {
    TournamentDTO request =
        new TournamentDTO(TOURNAMENT_NAME, REWARD_AMOUNT, REWARD_CURRENCY.toString());
    Tournament mockTournament = new Tournament(TOURNAMENT_NAME, REWARD_AMOUNT, REWARD_CURRENCY);
    when(tournamentService.createTournament(any(Tournament.class))).thenReturn(mockTournament);

    ResponseEntity<TournamentDTO> response = tournamentController.addTournament(request);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(TOURNAMENT_NAME, response.getBody().getName());
  }

  @Test
  void addTournament_InvalidCurrency_ShouldReturnBadRequest() {
    TournamentDTO request = new TournamentDTO(TOURNAMENT_NAME, REWARD_AMOUNT, "Invalid Currency");

    ResponseEntity<TournamentDTO> response = tournamentController.addTournament(request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void updateTournamentDetails_ValidUpdate_ShouldUpdateTournament() {
    TournamentDTO request =
        new TournamentDTO(TOURNAMENT_NAME, REWARD_AMOUNT, REWARD_CURRENCY.toString());
    when(tournamentService.updateTournamentDetails(eq(TOURNAMENT_ID), any(TournamentDTO.class)))
        .thenReturn(request);

    ResponseEntity<TournamentDTO> response =
        tournamentController.updateTournamentDetails(TOURNAMENT_ID, request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(TOURNAMENT_NAME, response.getBody().getName());
  }

  @Test
  void addPlayerToTournament_Success_ShouldAddPlayer() {
    ExerciseDTO mockResponse =
        new ExerciseDTO(
            TOURNAMENT_ID,
            TOURNAMENT_NAME,
            REWARD_AMOUNT,
            REWARD_CURRENCY.toString(),
            PLAYER_ID,
            PLAYER_NAME);
    when(tournamentService.addPlayerToTournament(PLAYER_ID, TOURNAMENT_ID))
        .thenReturn(mockResponse);

    ResponseEntity<ExerciseDTO> response =
        tournamentController.addPlayerToTournament(TOURNAMENT_ID, PLAYER_ID);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(TOURNAMENT_ID, response.getBody().getTournamentId());
    assertEquals(PLAYER_ID, response.getBody().getPlayerId());
  }

  @Test
  void deletePlayer() {
    ResponseEntity<HttpStatus> response = tournamentController.deleteTournament(123);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(tournamentService, times(1)).deleteTournament(123);
  }
}
