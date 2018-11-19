# --- !Ups

CREATE TABLE IF NOT EXISTS Users(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  firstName varchar(255) NOT NULL,
  lastName varchar(255) NOT NULL,
  email varchar(255) NOT NULL UNIQUE,
  password varchar(255) NOT NULL,
  userRole varchar(255) NOT NULL
);

INSERT INTO Users(lastName, firstName, password, email, userRole) VALUES ('Saad', 'Ali', 'asdasdasd', 'spadsdditcom', 'USER');
INSERT INTO Users(lastName, firstName, password, email, userRole) VALUES ('Fahad', 'Siddiqui', 'asdasdasd', 'xyz@abc.com', 'USER');

# --- !Downs

DROP TABLE IF EXISTS Users
