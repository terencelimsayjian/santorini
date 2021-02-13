create table game (
    id varchar(255) PRIMARY KEY ,
    player_1 varchar(255),
    player_2 varchar(255),
    current_player varchar(255),
    winner varchar(255),
    created_at timestamp,
    updated_at timestamp
)
