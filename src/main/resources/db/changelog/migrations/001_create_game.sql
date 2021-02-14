CREATE
    TABLE
        game(
            id VARCHAR(255) PRIMARY KEY,
            game_board jsonb,
            player_1 VARCHAR(255),
            player_2 VARCHAR(255),
            current_player VARCHAR(255),
            winner VARCHAR(255),
            created_at TIMESTAMP,
            updated_at TIMESTAMP
        )
