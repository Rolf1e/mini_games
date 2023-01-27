CREATE TABLE games (
    id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    min_players INTEGER NOT NULL DEFAULT 2,
    max_players INTEGER NOT NULL DEFAULT 2,
    created_at DATE NOT NULL DEFAULT now(),
    updated_at DATE NOT NULL DEFAULT now()
);

CREATE TABLE games_instances (
    id SERIAL PRIMARY KEY,
    players_id INTEGER[] NOT NULL,
    state JSON NOT NULL,
    instance_id SERIAL NOT NULL,
    created_at DATE NOT NULL DEFAULT now(),
    updated_at DATE NOT NULL DEFAULT now()
);
