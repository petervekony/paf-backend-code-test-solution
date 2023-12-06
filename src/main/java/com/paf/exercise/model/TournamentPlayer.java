package com.paf.exercise.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.paf.exercise.dto.TournamentPlayerDTO;

import lombok.Data;

@Entity
@Table(name = "tournament_player")
@IdClass(TournamentPlayerId.class)
@Data
public class TournamentPlayer {

  @Id
  @ManyToOne
  @JoinColumn(name = "tournament_id")
  private Tournament tournament;

  @Id
  @ManyToOne
  @JoinColumn(name = "player_id")
  private Player player;

  public TournamentPlayerDTO convertToDTO() {
    return new TournamentPlayerDTO(
        tournament.getId(),
        tournament.getRewardAmount(),
        tournament.getRewardCurrency().toString(),
        player.getId(),
        player.getName());
  }
}
