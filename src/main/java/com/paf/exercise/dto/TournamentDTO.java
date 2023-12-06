package com.paf.exercise.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDTO {
  private Integer id;
  private String name;
  private Double rewardAmount;
  private String rewardCurrency;
  private List<Integer> players;

  public TournamentDTO(String name, Double rewardAmount, String rewardCurrency) {
    this.name = name;
    this.rewardAmount = rewardAmount;
    this.rewardCurrency = rewardCurrency;
    this.players = new ArrayList<>();
  }
}
