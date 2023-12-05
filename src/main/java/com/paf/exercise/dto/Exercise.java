package com.paf.exercise.dto;

import java.util.Currency;

import com.paf.exercise.model.Player;

import lombok.Data;

@Data
public class Exercise {
  private Integer tournamentId;

  private int rewardAmount;

  private Currency rewardCurrency;

  private Player player;

  private String playerName;
}
