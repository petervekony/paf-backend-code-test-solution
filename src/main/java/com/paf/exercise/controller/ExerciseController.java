package com.paf.exercise.exercise.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paf.exercise.exercise.model.Exercise;
import com.paf.exercise.exercise.repository.ExerciseRepository;

@RestController
@RequestMapping("/")
public class ExerciseController {

  private final JdbcTemplate jdbcTemplate;
  private final ExerciseRepository exerciseRepository;

  @Autowired
  public ExerciseController(JdbcTemplate jdbcTemplate, ExerciseRepository exerciseRepository) {
    this.jdbcTemplate = jdbcTemplate;
    this.exerciseRepository = exerciseRepository;
  }

  @GetMapping("/exercise")
  public List<Exercise> getTournament(@RequestParam Integer tournamentId) {
    return exerciseRepository.findByTournamentId(tournamentId);
  }

  @GetMapping("/exercise")
  public List<Exercise> exercise(
      @RequestParam String operation,
      @RequestParam(required = false) Integer tournament_id,
      @RequestParam(required = false) Integer reward_amount,
      @RequestParam(required = false) Integer player_id,
      @RequestParam(required = false) String player_name) {
    List<Exercise> list = new ArrayList<>();
    Random random = new Random();
    if (operation.equals("getTournaments")) {
      list =
          jdbcTemplate.query(
              "select * from exercise where player_id is null",
              new RowMapper<Exercise>() {
                @Override
                public Exercise mapRow(ResultSet resultSet, int i) throws SQLException {
                  Exercise e = new Exercise();
                  e.setTournamentId(resultSet.getInt("tournament_id"));
                  e.setRewardAmount(resultSet.getInt("reward_amount"));
                  return e;
                }
              });
    } else if (operation.contains("getTournament")) {
      Exercise e =
          jdbcTemplate.queryForObject(
              "select * from exercise where tournament_id=" + tournament_id,
              new RowMapper<Exercise>() {
                @Override
                public Exercise mapRow(ResultSet resultSet, int i) throws SQLException {
                  Exercise e = new Exercise();
                  e.setTournamentId(resultSet.getInt("tournament_id"));
                  e.setRewardAmount(resultSet.getInt("reward_amount"));
                  return e;
                }
              });
      list.add(e);
    } else if (operation.equals("addTournament")) {
      int id = random.nextInt();
      int tournamentId = random.nextInt();
      jdbcTemplate.execute(
          "insert into exercise(id, tournament_id, reward_amount) "
              + "values("
              + id
              + ", "
              + tournamentId
              + ", "
              + reward_amount
              + ")");
    } else if (operation.equals("updateTournament")) {
      jdbcTemplate.execute(
          "update exercise set reward_amount="
              + reward_amount
              + " where tournament_id="
              + tournament_id);
    } else if (operation.equals("removeTournament")) {
      jdbcTemplate.execute("delete from exercise where tournament_id=" + tournament_id);
    } else if (operation.equals("addPlayerIntoTournament")) {
      int id = random.nextInt();
      int playerId = random.nextInt();
      jdbcTemplate.execute(
          "insert into exercise(id, tournament_id, player_id, player_name) "
              + "values("
              + id
              + ", "
              + tournament_id
              + ", "
              + playerId
              + ", '"
              + player_name
              + "')");
    } else if (operation.equals("removePlayerFromTournament")) {
      jdbcTemplate.execute(
          "delete from exercise where tournament_id="
              + tournament_id
              + " and player_id="
              + player_id);
    } else if (operation.equals("getPlayersInTournament")) {
      list =
          jdbcTemplate.query(
              "select * from exercise where tournament_id="
                  + tournament_id
                  + " and player_id is not null",
              new RowMapper<Exercise>() {
                @Override
                public Exercise mapRow(ResultSet resultSet, int i) throws SQLException {
                  Exercise e = new Exercise();
                  e.setTournamentId(resultSet.getInt("tournament_id"));
                  e.setPlayerId(resultSet.getInt("player_id"));
                  e.setPlayerName(resultSet.getString("player_name"));
                  return e;
                }
              });
    }
    return list;
  }
}
