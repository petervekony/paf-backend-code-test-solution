package com.paf.exercise.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class TournamentFlowIntegrationTests {
  private static final String JSON = "application/json";
  private static final String TOURNAMENTS = "/api/tournaments";
  private static final String PLAYERS = "/api/players";

  private static String tournamentId;
  private static String playerId;

  @Autowired private MockMvc mockMvc;

  @Test
  @Order(1)
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

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(responseBody);
    int id = rootNode.path("id").asInt();
    this.tournamentId = String.valueOf(id);
  }

  @Test
  @Order(2)
  void createPlayerTest() throws Exception {
    MvcResult result =
        mockMvc
            .perform(post(PLAYERS).contentType(JSON).content("{\"name\":\"John Doe\"}"))
            .andExpect(status().isCreated())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(responseBody);
    int id = rootNode.path("id").asInt();
    this.playerId = String.valueOf(id);

    mockMvc.perform(get(PLAYERS + "/" + this.playerId)).andExpect(status().isOk());
  }

  @Test
  @Order(3)
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
  @Order(4)
  void removePlayerFromTournament() throws Exception {
    mockMvc
        .perform(delete(TOURNAMENTS + "/" + this.tournamentId + "/players/" + this.playerId))
        .andExpect(status().isOk());
    mockMvc
        .perform(get(TOURNAMENTS + "/" + this.tournamentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.players", hasSize(0)));
  }

  @Test
  @Order(5)
  void deletePlayer() throws Exception {
    mockMvc.perform(delete(PLAYERS + "/" + this.playerId)).andExpect(status().isOk());
    mockMvc.perform(get(PLAYERS + "/" + this.playerId)).andExpect(status().isNotFound());
  }

  @Test
  @Order(6)
  void deleteTournament() throws Exception {
    mockMvc.perform(delete(TOURNAMENTS + "/" + this.tournamentId)).andExpect(status().isOk());
    mockMvc.perform(get(TOURNAMENTS + "/" + this.tournamentId)).andExpect(status().isNotFound());
  }
}
