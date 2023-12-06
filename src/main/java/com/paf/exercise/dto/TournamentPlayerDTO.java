package com.paf.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentPlayerDTO {
  Integer tournamentId;
  Double rewardAmount;
  String rewardCurrency;
  Integer playerId;
  String playerName;
}
