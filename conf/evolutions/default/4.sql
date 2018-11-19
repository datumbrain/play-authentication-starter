# --- !Ups

ALTER TABLE Users RENAME TO Users_old;
CREATE TABLE IF NOT EXISTS Users(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  email varchar(255) NOT NULL UNIQUE,
  password varchar(255) NOT NULL,
  userRole varchar(255) NOT NULL,
  validation_token varchar(255) NOT NULL Default(''),
  validated INTEGER  NOT NULL DEFAULT(0)
);

INSERT INTO Users (id, email, password, userRole, validation_token, validated)
  SELECT id, email, password, userRole, validation_token, validated
  FROM Users_old;

DROP TABLE IF EXISTS Users_old;