package com.paf.exercise.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentPlayerId implements Serializable {
  private Integer tournament;
  private Integer player;
}
