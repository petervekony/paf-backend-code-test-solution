package com.paf.exercise.model;

import java.util.ArrayList;
import java.util.List;

import com.paf.exercise.dto.PlayerDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@Table(name = "player")
public class Player {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @NonNull
  @Size(min = 3, max = 255, message = "Invalid name")
  private String name;

  @ManyToMany
  @JoinTable(
      name = "tournament_player",
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
