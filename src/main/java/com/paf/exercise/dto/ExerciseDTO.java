package com.paf.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDTO {
  Integer tournamentId;
  String tournamentName;
  Double rewardAmount;
  String rewardCurrency;
  Integer playerId;
  String playerName;
}
