package com.paf.exercise.integration;

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

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class InvalidInputIntegrationTests {
  private static final String JSON = "application/json";
  private static final String TOURNAMENTS = "/api/tournaments";
  private static final String PLAYERS = "/api/players";

  @Autowired private MockMvc mockMvc;

  @Test
  @Order(1)
  public void createUserWithInvalidName() throws Exception {
    mockMvc
        .perform(post(PLAYERS).contentType(JSON).content("{\"name\":\"\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(2)
  public void createTournamentWithInvalidCurrency() throws Exception {
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
  public void createTournamentWithInvalidReward() throws Exception {
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
  public void createTournamentWithInvalidName() throws Exception {

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
  public void createValidTournamentAndAddANonExistingPlayer() throws Exception {
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
}
