DROP TABLE screenshots IF EXISTS;
DROP TABLE paths IF EXISTS;

CREATE TABLE screenshots (
  area VARCHAR(20),
  type VARCHAR(20),
  num INT
);

CREATE TABLE paths (
  area VARCHAR(20),
  type VARCHAR(20),
  path VARCHAR(100)
);
