package com.paf.exercise.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDTO {
  private Integer id;

  @NonNull
  @Size(max = 255, message = "Tournament name too long")
  private String name;

  @NonNull
  @Size(min = 0, message = "Invalid reward amount")
  private Double rewardAmount;

  @NonNull
  @Size(max = 10, message = "Invalid currency")
  private String rewardCurrency;

  private List<Integer> players;

  public TournamentDTO(String name, Double rewardAmount, String rewardCurrency) {
    this.name = name;
    this.rewardAmount = rewardAmount;
    this.rewardCurrency = rewardCurrency;
    this.players = new ArrayList<>();
  }
}
