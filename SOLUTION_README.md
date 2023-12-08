## Changes made to Maven setup

- simplified the path, from com.paf.exercise.exercise to com.paf.exercise
- changed Spring Boot version to 3.2.0 (the current latest)
- changed Java version to 17
- added some dependencies, namely:
  - spring-boot-starter-validation
  - spring-boot-starter-data-jpa
  - lombok
  - mockito-core (for testing)
  - byte-buddy (mockito-core dependency)
  - byte-buddy-agent (mockito-core-dependency)
  - jacoco (for test coverage reports)

## Database setup

There are three tables in the database, player, tournament and
tournament_player. The third one keeps track of the player participation in
tournaments.

```sql
CREATE TABLE tournament (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    reward_amount DECIMAL(10, 2),
    reward_currency VARCHAR(10) DEFAULT 'EUR'
);

CREATE TABLE player (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE tournament_player (
    tournament_id INT,
    player_id INT,
    PRIMARY KEY (tournament_id, player_id),
    FOREIGN KEY (tournament_id) REFERENCES tournament(id),
    FOREIGN KEY (player_id) REFERENCES player(id)
);
```

## Models and DTOs

### The application uses two models:

- Player:

```java
public class Player {
    private Integer id;
    private String name;
    private List<Tournament> tournaments;
}
```

- Tournament:

```java
public class Tournament {
    private Integer id;
    private String name;
    private Double rewardAmount;
    private Currency rewardCurrency;
    private List<Player> players;
}
```

The Currency enum only has "EUR" at this point, but any other currencies can be
added in the future.

### DTO classes:

- PlayerDTO:

```java
public class PlayerDTO {
    private Integer id;
    private String name;
}
```

- TournamentDTO:

```java
public class TournamentDTO {
    private Integer id;
    private String name;
    private Double rewardAmount;
    private String rewardCurrency;
    private List<Integer> players;
}
```

In the DTO the player details are simplified to IDs.

- ExerciseDTO:

```java
public class ExerciseDTO {
  Integer tournamentId;
  String tournamentName;
  Double rewardAmount;
  String rewardCurrency;
  Integer playerId;
  String playerName;
}
```

This DTO is used as a response when a player is added to a tournament.

## Redesigned endpoints

There are two main endpoints:

- /api/players
- /api/tournaments

### Player endpoints

#### GET: /api/players

An empty GET request retrieves all players as a list of PlayerDTO objects, or
returns an empty list.

#### GET: /api/players/_{id}_

An empty GET request retrieves the player specified by {id}, or returns a 404
HTTP code.

#### POST: /api/players

A POST request with a PlayerDTO object in the body will create a player after
validation. If validation fails, it returns a 400 HTTP code. If successful,
returns the PlayerDTO with the id.

#### PUT: /api/players/_{id}_

A PUT request with a PlayerDTO object in the body will update the player
specified by {id}. If validation fails, it returns a 400 HTTP code. If
successful, returns the updated player details in a PlayerDTO object.

#### DELETE: /api/players/_{id}_

A DELETE request deletes the player specified by {id}. It returns a 204 HTTP
code.

### Tournament endpoints

#### GET: /api/tournaments || /api/tournaments?isEmpty=_{boolean}_

A GET request sent to the endpoint retrieves a list of tournaments. If the
"isEmpty" request parameter is omitted, the endpoint returns all tournaments. If
the "isEmpty" is true, the endpoint returns the tournaments without players
assigned. If "isEmpty" is false, it returns only the tournaments with at least
one player assigned.

#### GET: /api/tournaments/_{id}_

A GET request retrieves the tournament specified by {id}, or 404 HTTP code if
not found.

#### GET: /api/tournaments/_{tournamentId}_/players

A GET request retrieves all the player's details who participate in the
tournament specified by {tournamentId}.

#### POST: /api/tournaments

A POST request with a TournamentDTO object in the request body creates the
tournament after validation. If validation fails, it returns a 400 HTTP code.

#### PUT: /api/tournaments/_{id}_

A PUT request with a TournamentDTO object in the request body updates the
tournament after validation. If validation fails, returns a 400 HTTP code.

#### PUT: /api/tournaments/_{tournamentId}_/players/_{playerId}_

An empty PUT request adds the player specified by {playerId} to the tournament
specified by {tournamentId}. If either one is missing, it returns a 404 HTTP
code.

#### DELETE: /api/tournaments/_{tournamentId}_/players/_{playerId}_

A DELETE request removes the player specified by the {playerId} from the
tournament specified by {tournamentId}. If either of those cannot be found, or
if the player is not saved as a player in the tournament, it returns a 404 HTTP
code. If successful, returns 204.

#### DELETE: /api/tournaments/_{id}_

A DELETE request deletes the tournament specified by {id}. It returns 204 HTTP
code.
