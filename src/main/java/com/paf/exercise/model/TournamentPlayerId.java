package com.paf.exercise.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentPlayerId implements Serializable {
  private int tournament;
  private int player;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + tournament;
    result = prime * result + player;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    TournamentPlayerId other = (TournamentPlayerId) obj;
    if (tournament != other.tournament) return false;
    return player == other.player;
  }
}
