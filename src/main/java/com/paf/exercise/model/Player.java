package com.paf.exercise.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "player")
public class Player {
  @Id private int id;

  private String name;

  private String email;

  @ManyToMany
  @JoinTable(
      name = "player_tournament",
      joinColumns = @JoinColumn(name = "player_id"),
      inverseJoinColumns = @JoinColumn(name = "tournament_id"))
  private List<Tournament> tournaments = new ArrayList<>();

  public Player(String name, String email) {
    this.name = name;
    this.email = email;
  }
}
