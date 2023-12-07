package com.paf.exercise.integration.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntegrationTestUtils {

  public static String extractIdFromJsonString(String json) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(json);
    return String.valueOf(rootNode.path("id").asInt());
  }

  public static String extractNameFromJsonString(String json) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(json);
    return String.valueOf(rootNode.path("name").asText());
  }

  public static Double extractRewardAmountFromJsonString(String json)
      throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(json);
    return Double.valueOf(rootNode.path("rewardAmount").asDouble());
  }
}
