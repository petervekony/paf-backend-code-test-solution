package com.paf.exercise.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.paf.exercise.dto.TournamentDTO;
import com.paf.exercise.model.enums.Currency;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tournament")
public class Tournament {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String name;

  @Column(name = "reward_amount")
  private Double rewardAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "reward_currency")
  private Currency rewardCurrency;

  @ManyToMany(mappedBy = "tournaments")
  private List<Player> players = new ArrayList<>();

  public Tournament(String name, Double rewardAmount, Currency rewardCurrency) {
    this.name = name;
    this.rewardAmount = rewardAmount;
    this.rewardCurrency = rewardCurrency;
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
