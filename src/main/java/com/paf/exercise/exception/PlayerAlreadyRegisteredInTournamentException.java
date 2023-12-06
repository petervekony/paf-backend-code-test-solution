package com.paf.exercise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlayerAlreadyRegisteredInTournamentException extends RuntimeException {
  public PlayerAlreadyRegisteredInTournamentException(String message) {
    super(message);
  }
}
