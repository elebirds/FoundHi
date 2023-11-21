CREATE TABLE IF NOT EXISTS player_banned (
    id int PRIMARY KEY AUTO_INCREMENT,
    operator varchar(255) NOT NULL,
    banned varchar(255) NOT NULL,
    start_time datetime NOT NULL,
    end_time datetime NOT NULL,
    reason varchar(255) NOT NULL,
    be_lifted bool DEFAULT false,
    reviser varchar(255) NOT NULL
);