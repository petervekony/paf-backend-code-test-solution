package com.paf.exercise.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.paf.exercise.integration.utils.IntegrationTestUtils;
import com.paf.exercise.repository.PlayerRepository;
import com.paf.exercise.repository.TournamentRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class TournamentFlowIntegrationTests {
  private static final String JSON = "application/json";
  private static final String TOURNAMENTS = "/api/tournaments";
  private static final String PLAYERS = "/api/players";

  private static String tournamentId;
  private static String playerId;

  @Autowired private MockMvc mockMvc;

  @Autowired private PlayerRepository playerRepository;
  @Autowired private TournamentRepository tournamentRepository;

  @BeforeAll
  void resetDB() {
    playerRepository.deleteAll();
    tournamentRepository.deleteAll();
  }

  @Test
  @Order(1)
  void getAllPlayers_WhenNoPlayersExist_ShouldReturnNoContent() throws Exception {
    mockMvc.perform(get(PLAYERS)).andExpect(status().isOk());
  }

  @Test
  @Order(2)
  void getAllTournaments_WhenNoTournamentsExist_ShouldReturnNoContent() throws Exception {
    mockMvc.perform(get(TOURNAMENTS)).andExpect(status().isOk());
  }

  @Test
  @Order(3)
  void createTournamentTest() throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                post(TOURNAMENTS)
                    .contentType(JSON)
                    .content(
                        "{\"name\":\"New Tournament\", \"rewardAmount\":1000.00,"
                            + " \"rewardCurrency\":\"EUR\"}"))
            .andExpect(status().isCreated())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();

    this.tournamentId = IntegrationTestUtils.extractIdFromJsonString(responseBody);
  }

  @Test
  @Order(4)
  void getAllTournaments_WhenTournamentsExist_ShouldReturnTournamentsList() throws Exception {
    // Assuming tournaments have been created
    mockMvc
        .perform(get(TOURNAMENTS))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$", hasSize(1))); // Adjust size as per the number of tournaments created
  }

  @Test
  @Order(5)
  void updateTournamentDetails() throws Exception {
    mockMvc
        .perform(
            put(TOURNAMENTS + "/" + this.tournamentId)
                .contentType(JSON)
                .content(
                    "{\"name\":\"Updated Name\", \"rewardAmount\":999.99,"
                        + " \"rewardCurrency\":\"EUR\"}"))
        .andExpect(status().isOk());

    MvcResult result =
        mockMvc
            .perform(get(TOURNAMENTS + "/" + this.tournamentId))
            .andExpect(status().isOk())
            .andReturn();
    String responseBody = result.getResponse().getContentAsString();
    Double resultRewardAmount =
        IntegrationTestUtils.extractRewardAmountFromJsonString(responseBody);
    String resultName = IntegrationTestUtils.extractNameFromJsonString(responseBody);

    assertEquals(999.99, resultRewardAmount);
    assertEquals("Updated Name", resultName);
  }

  @Test
  @Order(6)
  void createPlayerTest() throws Exception {
    MvcResult result =
        mockMvc
            .perform(post(PLAYERS).contentType(JSON).content("{\"name\":\"John Doe\"}"))
            .andExpect(status().isCreated())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();

    this.playerId = IntegrationTestUtils.extractIdFromJsonString(responseBody);

    mockMvc.perform(get(PLAYERS + "/" + this.playerId)).andExpect(status().isOk());
  }

  @Test
  @Order(7)
  void getAllPlayers_WhenPlayersExist_ShouldReturnPlayersList() throws Exception {
    // Assuming players have been created
    mockMvc
        .perform(get(PLAYERS))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1))); // Adjust size as per the number of players created
  }

  @Test
  @Order(8)
  void updatePlayerName() throws Exception {
    mockMvc
        .perform(
            put(PLAYERS + "/" + this.playerId).contentType(JSON).content("{\"name\":\"Jane Doe\"}"))
        .andExpect(status().isOk());

    MvcResult updated =
        mockMvc.perform(get(PLAYERS + "/" + this.playerId)).andExpect(status().isOk()).andReturn();
    String responseBody = updated.getResponse().getContentAsString();
    String updatedName = IntegrationTestUtils.extractNameFromJsonString(responseBody);
    assertEquals("Jane Doe", updatedName);
  }

  @Test
  @Order(9)
  void addPlayerToTournament() throws Exception {
    mockMvc
        .perform(put(TOURNAMENTS + "/" + this.tournamentId + "/players/" + this.playerId))
        .andExpect(status().isOk());
    mockMvc
        .perform(get(TOURNAMENTS + "/" + this.tournamentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.players", hasSize(1)));
  }

  @Test
  @Order(10)
  void removePlayerFromTournament() throws Exception {
    mockMvc
        .perform(delete(TOURNAMENTS + "/" + this.tournamentId + "/players/" + this.playerId))
        .andExpect(status().isNoContent());
    mockMvc
        .perform(get(TOURNAMENTS + "/" + this.tournamentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.players", hasSize(0)));
  }

  @Test
  @Order(11)
  void deletePlayer() throws Exception {
    mockMvc.perform(delete(PLAYERS + "/" + this.playerId)).andExpect(status().isNoContent());
    mockMvc.perform(get(PLAYERS + "/" + this.playerId)).andExpect(status().isNotFound());
  }

  @Test
  @Order(12)
  void deleteTournament() throws Exception {
    mockMvc
        .perform(delete(TOURNAMENTS + "/" + this.tournamentId))
        .andExpect(status().isNoContent());
    mockMvc.perform(get(TOURNAMENTS + "/" + this.tournamentId)).andExpect(status().isNotFound());
  }

  @Test
  @Order(13)
  void addMultiplePlayersToTournament_RemoveOneAfter() throws Exception {
    MvcResult tournamentResult =
        mockMvc
            .perform(
                post(TOURNAMENTS)
                    .contentType(JSON)
                    .content(
                        "{\"name\":\"Tournament\", \"rewardAmount\":1000.00,"
                            + " \"rewardCurrency\":\"EUR\"}"))
            .andExpect(status().isCreated())
            .andReturn();
    String resultBody = tournamentResult.getResponse().getContentAsString();
    String id = IntegrationTestUtils.extractIdFromJsonString(resultBody);

    // list of mock players
    List<String> playerIds = new ArrayList<>();

    // add players to the db
    for (int i = 1; i <= 3; i++) {
      MvcResult result =
          mockMvc
              .perform(post(PLAYERS).contentType(JSON).content("{\"name\":\"Player " + i + "\"}"))
              .andExpect(status().isCreated())
              .andReturn();
      playerIds.add(
          IntegrationTestUtils.extractIdFromJsonString(result.getResponse().getContentAsString()));
    }

    // add players to the tournament
    for (String playerId : playerIds) {
      mockMvc
          .perform(put(TOURNAMENTS + "/" + id + "/players/" + playerId))
          .andExpect(status().isOk());
    }

    mockMvc
        .perform(get(TOURNAMENTS + "/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.players", hasSize(3)));

    mockMvc
        .perform(delete(TOURNAMENTS + "/" + id + "/players/" + playerIds.get(0)))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get(TOURNAMENTS + "/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.players", hasSize(2)));
  }

  @Test
  @Order(14)
  void updatePlayerTest() throws Exception {
    MvcResult createResult =
        mockMvc
            .perform(post(PLAYERS).contentType(JSON).content("{\"name\":\"Original Name\"}"))
            .andExpect(status().isCreated())
            .andReturn();

    String playerId =
        IntegrationTestUtils.extractIdFromJsonString(
            createResult.getResponse().getContentAsString());

    mockMvc
        .perform(
            put(PLAYERS + "/" + playerId).contentType(JSON).content("{\"name\":\"Updated Name\"}"))
        .andExpect(status().isOk());

    mockMvc
        .perform(get(PLAYERS + "/" + playerId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Name"));
  }
}
