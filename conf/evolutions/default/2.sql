# --- !Ups

CREATE TABLE IF NOT EXISTS ForgotPassword(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER NOT NULL,
  token varchar(255) NOT NULL UNIQUE,
  time_sent TIMESTAMP NOT NULL,
  verified INTEGER NOT NULL Default(0),
  FOREIGN KEY(user_id) REFERENCES Users(id)
);

# --- !Downs

DROP TABLE IF EXISTS ForgotPassword;
