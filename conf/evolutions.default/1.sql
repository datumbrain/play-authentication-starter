# --- !Ups

CREATE TABLE IF NOT EXISTS Users(
id INTEGER PRIMARY KEY AUTOINCREMENT,
lastName varchar(255) NOT NULL,
firstName varchar(255) NOT NULL,
password varchar(255) NOT NULL,
email varchar(255) NOT NULL UNIQUE,
userRole varchar(255) NOT NULL,
Authenticated boolean
);

INSERT INTO Users(LastName, FirstName, Password, Email, Authenticated) VALUES ('Saad', 'Ali', 'spadsdditcom', 'asdasdasd', 'USER' ,1);

# --- !Downs

DROP TABLE IF EXISTS Users