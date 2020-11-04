package com.paf.exercise.exercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@RestController
public class ExerciseApplication {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@GetMapping("/exercise")
	public List<Exercise> exercise(@RequestParam String operation,
								   @RequestParam(required = false) Integer tournament_id,
								   @RequestParam(required = false) Integer reward_amount,
								   @RequestParam(required = false) Integer player_id,
								   @RequestParam(required = false) String player_name) {
		List<Exercise> list = new ArrayList<>();
		if (operation.equals("getTournaments")) {
			list = jdbcTemplate.query("select * from exercise where player_id is null", new RowMapper<Exercise>() {
				@Override
				public Exercise mapRow(ResultSet resultSet, int i) throws SQLException {
					Exercise e = new Exercise();
					e.tournament_id = resultSet.getInt("tournament_id");
					e.reward_amount = resultSet.getInt("reward_amount");
					return e;
				}
			});
		} else if (operation.contains("getTournament")) {
			Exercise e = jdbcTemplate.queryForObject("select * from exercise where tournament_id=" + tournament_id,
					new RowMapper<Exercise>() {
						@Override
						public Exercise mapRow(ResultSet resultSet, int i) throws SQLException {
							Exercise e = new Exercise();
							e.tournament_id = resultSet.getInt("tournament_id");
							e.reward_amount = resultSet.getInt("reward_amount");
							return e;
						}
					});
			list.add(e);
		} else if (operation.equals("addTournament")) {
			Random random = new Random();
			int id = random.nextInt();
			int tournamentId = random.nextInt();
			jdbcTemplate.execute("insert into exercise(id, tournament_id, reward_amount) " +
					"values(" + id + ", " + tournamentId + ", " + reward_amount + ")");
		} else if (operation.equals("updateTournament")) {
			jdbcTemplate.execute("update exercise set reward_amount=" + reward_amount +
					" where tournament_id=" + tournament_id);
		} else if (operation.equals("removeTournament")) {
			jdbcTemplate.execute("delete from exercise where tournament_id=" + tournament_id);
		} else if (operation.equals("addPlayerIntoTournament")) {
			Random random = new Random();
			int id = random.nextInt();
			int playerId = random.nextInt();
			jdbcTemplate.execute("insert into exercise(id, tournament_id, player_id, player_name) " +
					"values(" + id + ", " + tournament_id + ", " + playerId + ", '" + player_name + "')");
		} else if (operation.equals("removePlayerFromTournament")) {
			jdbcTemplate.execute("delete from exercise where tournament_id=" + tournament_id
					+ " and player_id=" + player_id);
		} else if (operation.equals("getPlayersInTournament")) {
			list = jdbcTemplate.query("select * from exercise where tournament_id=" + tournament_id +
					" and player_id is not null", new RowMapper<Exercise>() {
				@Override
				public Exercise mapRow(ResultSet resultSet, int i) throws SQLException {
					Exercise e = new Exercise();
					e.tournament_id = resultSet.getInt("tournament_id");
					e.player_id = resultSet.getInt("player_id");
					e.player_name = resultSet.getString("player_name");
					return e;
				}
			});
		}
		return list;
	}

	public static void main(String[] args) {
		SpringApplication.run(ExerciseApplication.class, args);
	}

	private class Exercise {
		public int tournament_id;
		public int reward_amount;
		public int player_id;
		public String player_name;
	}
}
