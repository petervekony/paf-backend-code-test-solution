package com.paf.exercise.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.paf.exercise.dto.ExerciseDTO;
import com.paf.exercise.dto.PlayerDTO;
import com.paf.exercise.dto.TournamentDTO;
import com.paf.exercise.exception.NotFoundException;
import com.paf.exercise.exception.PlayerAlreadyRegisteredInTournamentException;
import com.paf.exercise.model.Player;
import com.paf.exercise.model.Tournament;
import com.paf.exercise.model.enums.Currency;
import com.paf.exercise.repository.PlayerRepository;
import com.paf.exercise.repository.TournamentRepository;
import com.paf.exercise.service.TournamentService;

@SpringBootTest
class TournamentServiceTests {
  private static final String NAME = "Tournament";
  private static final Double REWARD_AMOUNT = 999.99;
  private static final Currency REWARD_CURRENCY = Currency.EUR;

  private static final String NAME_2 = "Tournament2";
  private static final Double REWARD_AMOUNT_2 = 123.23;

  private static final String JOHN_DOE = "John Doe";
  private static final String JANE_DOE = "Jane Doe";

  @Mock private TournamentRepository tournamentRepository;
  @Mock private PlayerRepository playerRepository;

  @InjectMocks private TournamentService tournamentService;

  @BeforeEach
  void setUp() {
    tournamentService = new TournamentService(tournamentRepository, playerRepository);
  }

  @Test
  void testCreateTournament() {
    Tournament mockTournament = new Tournament(NAME, REWARD_AMOUNT, REWARD_CURRENCY);
    when(tournamentRepository.save(any(Tournament.class))).thenReturn(mockTournament);

    Tournament createdTournament = tournamentService.createTournament(mockTournament);

    assertNotNull(createdTournament);
    assertEquals(NAME, createdTournament.getName());
    assertEquals(REWARD_AMOUNT, createdTournament.getRewardAmount());
    assertEquals(REWARD_CURRENCY, createdTournament.getRewardCurrency());
  }

  @Test
  void testGetTournamentSuccess() {
    Integer tournamentId = 1;
    Tournament mockTournament = new Tournament(NAME, REWARD_AMOUNT, REWARD_CURRENCY);
    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(mockTournament));

    Tournament foundTournament = tournamentService.findTournament(tournamentId);

    assertNotNull(foundTournament);
    assertEquals(mockTournament, foundTournament);
  }

  @Test
  void testGetTournamentNotFound() {
    Integer tournamentId = 1;
    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> {
          tournamentService.findTournament(tournamentId);
        });
  }

  @Test
  void testGetAllTournaments() {
    List<Tournament> mockTournaments =
        Arrays.asList(
            new Tournament(NAME, REWARD_AMOUNT, REWARD_CURRENCY),
            new Tournament(NAME_2, REWARD_AMOUNT_2, REWARD_CURRENCY));
    when(tournamentRepository.findAll()).thenReturn(mockTournaments);

    List<Tournament> tournaments = tournamentService.findAllTournaments();

    assertNotNull(tournaments);
    assertEquals(2, tournaments.size());
  }

  @Test
  void testGetAllEmptyTournaments() {

    List<Tournament> mockTournaments =
        Arrays.asList(
            new Tournament(NAME, REWARD_AMOUNT, REWARD_CURRENCY),
            new Tournament(NAME_2, REWARD_AMOUNT_2, REWARD_CURRENCY));
    when(tournamentRepository.findTournamentsWithoutPlayers()).thenReturn(mockTournaments);

    List<Tournament> tournaments = tournamentService.findEmptyTournaments();

    assertNotNull(tournaments);
    assertEquals(2, tournaments.size());
  }

  @Test
  void testGetAllNonEmptyTournaments() {

    List<Tournament> mockTournaments =
        Arrays.asList(
            new Tournament(NAME, REWARD_AMOUNT, REWARD_CURRENCY),
            new Tournament(NAME_2, REWARD_AMOUNT_2, REWARD_CURRENCY));
    Player mockPlayer1 = new Player(JOHN_DOE);
    Player mockPlayer2 = new Player(JANE_DOE);

    mockTournaments.get(0).getPlayers().add(mockPlayer1);
    mockTournaments.get(1).getPlayers().add(mockPlayer2);
    when(tournamentRepository.findTournamentsWithPlayers()).thenReturn(mockTournaments);

    List<Tournament> tournaments = tournamentService.findNonEmptyTournaments();

    assertNotNull(tournaments);
    assertEquals(2, tournaments.size());
  }

  @Test
  void testAddPlayerToTournamentSuccess() {
    Integer playerId = 1;
    Integer tournamentId = 2;
    Player mockPlayer = new Player(JOHN_DOE);
    mockPlayer.setId(playerId);
    Tournament mockTournament = new Tournament(NAME, REWARD_AMOUNT, REWARD_CURRENCY);
    mockTournament.setId(tournamentId);

    when(playerRepository.findById(playerId)).thenReturn(Optional.of(mockPlayer));
    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(mockTournament));

    ExerciseDTO mockDTO =
        new ExerciseDTO(
            mockTournament.getId(),
            mockTournament.getName(),
            mockTournament.getRewardAmount(),
            mockTournament.getRewardCurrency().toString(),
            mockPlayer.getId(),
            mockPlayer.getName());
    when(playerRepository.save(any(Player.class))).thenReturn(mockPlayer);
    when(tournamentRepository.save(any(Tournament.class))).thenReturn(mockTournament);

    ExerciseDTO DTO =
        tournamentService.addPlayerToTournament(mockPlayer.getId(), mockTournament.getId());

    assertNotNull(DTO);
    assertEquals(mockDTO, DTO);
  }

  @Test
  void testAddPlayerToTournamentPlayerNotFound() {
    Integer tournamentId = 2;
    Tournament mockTournament = new Tournament(NAME, REWARD_AMOUNT, REWARD_CURRENCY);
    mockTournament.setId(tournamentId);
    when(playerRepository.findById(anyInt())).thenReturn(Optional.empty());
    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(mockTournament));

    assertThrows(
        NotFoundException.class,
        () -> {
          tournamentService.addPlayerToTournament(123, tournamentId);
        });
  }

  @Test
  void testAddPlayerToTournamentTournamentNotFound() {
    Integer playerId = 1;
    Player mockPlayer = new Player(JOHN_DOE);
    mockPlayer.setId(playerId);

    when(playerRepository.findById(playerId)).thenReturn(Optional.of(mockPlayer));
    when(tournamentRepository.findById(anyInt())).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> {
          tournamentService.addPlayerToTournament(playerId, 123);
        });
  }

  @Test
  void testDeleteTournament() {
    Integer tournamentId = 2;
    doNothing().when(tournamentRepository).deleteById(tournamentId);

    assertDoesNotThrow(() -> tournamentService.deleteTournament(tournamentId));
  }

  @Test
  void testUpdateTournamentSuccess() {
    Integer tournamentId = 2;
    Tournament mockTournament = new Tournament(NAME, REWARD_AMOUNT, REWARD_CURRENCY);
    TournamentDTO tournamentDTO =
        new TournamentDTO(NAME_2, REWARD_AMOUNT_2, REWARD_CURRENCY.toString());

    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(mockTournament));
    when(tournamentRepository.save(any(Tournament.class)))
        .thenAnswer(i -> i.getArgument(0, Tournament.class));

    TournamentDTO updatedTournament =
        tournamentService.updateTournamentDetails(tournamentId, tournamentDTO);

    assertNotNull(updatedTournament);
    assertEquals(tournamentDTO.getName(), updatedTournament.getName());
    assertEquals(tournamentDTO.getRewardAmount(), updatedTournament.getRewardAmount());
  }

  @Test
  void testUpdateTournamentNotFound() {
    Integer tournamentId = 2;
    TournamentDTO tournamentDTO =
        new TournamentDTO(NAME, REWARD_AMOUNT, REWARD_CURRENCY.toString());

    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> {
          tournamentService.updateTournamentDetails(tournamentId, tournamentDTO);
        });
  }

  @Test
  void testAddPlayerToTournament_WhenPlayerAlreadyRegistered() {
    int playerId = 1;
    int tournamentId = 1;

    Player mockPlayer = new Player(JOHN_DOE);
    mockPlayer.setId(playerId);
    Tournament mockTournament = new Tournament("Tournament", 1000.00, Currency.EUR);
    mockTournament.setId(tournamentId);
    mockTournament.getPlayers().add(mockPlayer);

    when(playerRepository.findById(playerId)).thenReturn(Optional.of(mockPlayer));
    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(mockTournament));

    assertThrows(
        PlayerAlreadyRegisteredInTournamentException.class,
        () -> tournamentService.addPlayerToTournament(playerId, tournamentId));
  }

  @Test
  void testGetAllPlayersInTournament_WhenTournamentHasPlayers() {
    Integer tournamentId = 1;
    Player mockPlayer1 = new Player(JOHN_DOE);
    mockPlayer1.setId(1);
    Player mockPlayer2 = new Player(JANE_DOE);
    mockPlayer2.setId(2);

    Tournament mockTournament = new Tournament("Tournament", 1000.00, Currency.EUR);
    mockTournament.setId(tournamentId);
    mockTournament.getPlayers().addAll(Arrays.asList(mockPlayer1, mockPlayer2));

    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(mockTournament));

    List<PlayerDTO> players = tournamentService.getAllPlayersInTournament(tournamentId);

    assertNotNull(players);
    assertEquals(2, players.size());
    assertTrue(players.stream().anyMatch(p -> p.getId().equals(mockPlayer1.getId())));
    assertTrue(players.stream().anyMatch(p -> p.getId().equals(mockPlayer2.getId())));
  }

  @Test
  void testGetAllPlayersInTournament_WhenTournamentNotFound() {
    Integer tournamentId = 1;
    when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> tournamentService.getAllPlayersInTournament(tournamentId));
  }
}
