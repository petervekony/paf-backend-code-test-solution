package com.paf.exercise.exception;

public class PlayerAlreadyRegisteredInTournamentException extends RuntimeException {
  public PlayerAlreadyRegisteredInTournamentException(String message) {
    super(message);
  }
}
