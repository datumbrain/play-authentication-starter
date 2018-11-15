# --- !Ups

CREATE TABLE IF NOT EXISTS Users(
id INTEGER PRIMARY KEY AUTOINCREMENT,
firstName varchar(255) NOT NULL,
lastName varchar(255) NOT NULL,
email varchar(255) NOT NULL UNIQUE,
password varchar(255) NOT NULL,
userRole varchar(255) NOT NULL,
authenticated boolean
);

INSERT INTO Users(LastName, FirstName, Password, Email, userRole, authenticated) VALUES ('Saad', 'Ali', 'spadsdditcom', 'asdasdasd', 'USER' ,1);
INSERT INTO Users(LastName, FirstName, Password, Email, userRole, authenticated) VALUES ('Fahad', 'Siddiqui', 'xyz@abc.com', 'asdasdasd', 'USER' ,1);


# --- !Downs

DROP TABLE IF EXISTS Users
