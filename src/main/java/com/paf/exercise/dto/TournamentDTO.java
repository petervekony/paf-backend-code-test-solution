package com.paf.exercise.dto;

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
}
