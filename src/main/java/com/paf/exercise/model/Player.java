package com.paf.exercise.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.paf.exercise.dto.PlayerDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "player")
public class Player {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String name;

  @ManyToMany
  @JoinTable(
      name = "player_tournament",
      joinColumns = @JoinColumn(name = "player_id"),
      inverseJoinColumns = @JoinColumn(name = "tournament_id"))
  private List<Tournament> tournaments = new ArrayList<>();

  public Player(String name) {
    this.name = name;
  }

  public PlayerDTO convertToDTO() {
    return new PlayerDTO(this.id, this.name);
  }
}
