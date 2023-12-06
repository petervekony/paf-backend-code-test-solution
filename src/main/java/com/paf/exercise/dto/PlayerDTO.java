package com.paf.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class PlayerDTO {
  Integer id;

  @NonNull String name;

  public PlayerDTO(String name) {
    this.name = name;
  }
}
