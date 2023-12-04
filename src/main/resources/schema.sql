CREATE TABLE tournament (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    reward_amount DECIMAL(10, 2),
    reward_currency VARCHAR(3) DEFAULT 'EUR'
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
