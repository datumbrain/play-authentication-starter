# --- !Ups

ALTER TABLE Users ADD validation_token varchar(255) NOT NULL Default('');
ALTER TABLE Users ADD validated INTEGER  NOT NULL DEFAULT(0);

# --- !Downs

ALTER TABLE Users RENAME TO Users_old;
CREATE TABLE IF NOT EXISTS Users(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  firstName varchar(255) NOT NULL,
  lastName varchar(255) NOT NULL,
  email varchar(255) NOT NULL UNIQUE,
  password varchar(255) NOT NULL,
  userRole varchar(255) NOT NULL
);

INSERT INTO Users (id, firstName, lastName, email, password, userRole)
  SELECT id, firstName, lastName, email, password, userRole
  FROM Users_old;

-- DROP TABLE IF EXISTS Users_old;