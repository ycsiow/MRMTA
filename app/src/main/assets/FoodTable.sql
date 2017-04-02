BEGIN TRANSACTION;
DROP TABLE IF EXISTS "Food";
CREATE TABLE "Food" ('_id' INTEGER, 'Name' TEXT, 'Type' TEXT, 'Course' TEXT, PRIMARY KEY('_id'));
INSERT INTO Food (_id,Name,Type,Course) VALUES (1,'nasi lemak','rice','main course');
INSERT INTO Food (_id,Name,Type,Course) VALUES (2,'nasi lemak','rice','main course');
INSERT INTO Food (_id,Name,Type,Course) VALUES (3,'pussy','pussy','main course');
COMMIT;