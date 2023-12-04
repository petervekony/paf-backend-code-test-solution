package com.paf.exercise.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "tournament")
public class Tournament {
  @Id private Integer id;
  private String name;

  @OneToMany(mappedBy = "tournament")
  private List<Player> players = new ArrayList<>();

  public Tournament(String name) {
    this.name = name;
  }
}
