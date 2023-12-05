package com.paf.exercise.model;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.paf.exercise.dto.TournamentDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tournament")
public class Tournament {
  @Id private Integer id;
  private String name;

  @Column(name = "reward_amount")
  private Double rewardAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "reward_currency")
  private Currency rewardCurrency;

  @OneToMany(mappedBy = "tournament", fetch = FetchType.LAZY)
  private List<Player> players = new ArrayList<>();

  public Tournament(String name) {
    this.name = name;
  }

  public TournamentDTO convertToDTO() {
    List<Integer> playerIds =
        this.getPlayers().stream().map(player -> player.getId()).collect(Collectors.toList());

    return new TournamentDTO(
        this.getId(),
        this.getName(),
        this.getRewardAmount(),
        this.getRewardCurrency().toString(),
        playerIds);
  }
}
