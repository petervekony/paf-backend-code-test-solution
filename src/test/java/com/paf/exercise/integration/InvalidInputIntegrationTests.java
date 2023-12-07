package com.paf.exercise.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.paf.exercise.integration.utils.IntegrationTestUtils;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class InvalidInputIntegrationTests {
  private static final String JSON = "application/json";
  private static final String TOURNAMENTS = "/api/tournaments";
  private static final String PLAYERS = "/api/players";
  private static final String JOHN_DOE = "John Doe";

  @Autowired private MockMvc mockMvc;

  @Test
  @Order(1)
  void createUserWithInvalidName() throws Exception {
    mockMvc
        .perform(post(PLAYERS).contentType(JSON).content("{\"name\":\"\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(2)
  void createTournamentWithInvalidCurrency() throws Exception {
    mockMvc
        .perform(
            post(TOURNAMENTS)
                .contentType(JSON)
                .content(
                    "{\"name\":\"New Tournament\", \"rewardAmount\":1000.00,"
                        + " \"rewardCurrency\":\"HUF\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(3)
  void createTournamentWithInvalidReward() throws Exception {
    mockMvc
        .perform(
            post(TOURNAMENTS)
                .contentType(JSON)
                .content(
                    "{\"name\":\"New Tournament\", \"rewardAmount\":-1.00,"
                        + " \"rewardCurrency\":\"EUR\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(4)
  void createTournamentWithInvalidName() throws Exception {

    mockMvc
        .perform(
            post(TOURNAMENTS)
                .contentType(JSON)
                .content(
                    "{\"name\":\"\", \"rewardAmount\":1000.00," + " \"rewardCurrency\":\"EUR\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(5)
  void createValidTournamentAndAddANonExistingPlayer() throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                post(TOURNAMENTS)
                    .contentType(JSON)
                    .content(
                        "{\"name\":\"Tournament\", \"rewardAmount\":1000.00,"
                            + " \"rewardCurrency\":\"EUR\"}"))
            .andExpect(status().isCreated())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(responseBody);
    String tournamentId = String.valueOf(rootNode.path("id").asInt());

    mockMvc
        .perform(put(TOURNAMENTS + "/" + tournamentId + "/players/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(6)
  void createValidPlayerAndUpdateWithInvalidName() throws Exception {
    MvcResult createdPlayerResult =
        mockMvc
            .perform(
                post(PLAYERS)
                    .contentType(JSON)
                    .content(String.format("{\"name\":\"%s\"}", JOHN_DOE)))
            .andExpect(status().isCreated())
            .andReturn();

    String responseBody = createdPlayerResult.getResponse().getContentAsString();

    String playerId = IntegrationTestUtils.extractIdFromJsonString(responseBody);

    mockMvc
        .perform(put(PLAYERS + "/" + playerId).contentType(JSON).content("{\"name\":\"a\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(7)
  void createValidTournamentAndUpdateWithInvalidName() throws Exception {
    MvcResult createdTournamentResult =
        mockMvc
            .perform(
                post(TOURNAMENTS)
                    .contentType(JSON)
                    .content(
                        "{\"name\":\"Original Name\", \"rewardAmount\":1000.00,"
                            + " \"rewardCurrency\":\"EUR\"}"))
            .andExpect(status().isCreated())
            .andReturn();

    String responseBody = createdTournamentResult.getResponse().getContentAsString();
    String id = IntegrationTestUtils.extractIdFromJsonString(responseBody);

    mockMvc.perform(get(TOURNAMENTS + "/" + id)).andExpect(status().isOk());

    mockMvc
        .perform(
            put(TOURNAMENTS + "/" + id)
                .contentType(JSON)
                .content(
                    "{\"name\":\"\", \"rewardAmount\":1000.00," + " \"rewardCurrency\":\"EUR\"}"))
        .andExpect(status().isBadRequest());
  }
}
