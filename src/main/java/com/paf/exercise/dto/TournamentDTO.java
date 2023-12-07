package com.paf.exercise.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
  @Size(min = 3, max = 255, message = "Tournament name too long")
  private String name;

  @NonNull
  @Min(value = 0, message = "Invalid reward amount")
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
