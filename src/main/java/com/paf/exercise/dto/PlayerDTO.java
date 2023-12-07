package com.paf.exercise.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
  Integer id;

  @NonNull
  @Size(min = 3, max = 255, message = "Invalid name")
  String name;

  public PlayerDTO(String name) {
    this.name = name;
  }
}
