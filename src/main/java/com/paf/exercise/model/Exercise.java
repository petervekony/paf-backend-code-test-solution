package com.paf.exercise.model;

import java.util.Currency;

import lombok.Data;

@Data
public class Exercise {
  private Integer tournamentId;

  private int rewardAmount;

  private Currency rewardCurrency;

  private Player player;

  private String playerName;
}
