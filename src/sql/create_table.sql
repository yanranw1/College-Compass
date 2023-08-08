CREATE database if not exists collegedb;
use collegedb;

CREATE TABLE school(
   id              VARCHAR(22) PRIMARY KEY
  ,name            VARCHAR(55) NOT NULL
  ,rating          FLOAT NOT NULL
  ,numVotes        INTEGER  NOT NULL
  ,net_cost        INTEGER  NOT NULL
  ,description     VARCHAR(483) NOT NULL
  ,upper_SAT       INTEGER  NOT NULL
  ,lower_SAT       INTEGER  NOT NULL
  ,link_to_website VARCHAR(80) NOT NULL
  ,telephone       VARCHAR(14) NOT NULL
  ,address         VARCHAR(100) NOT NULL
  ,link_to_image   VARCHAR(82) NOT NULL
);

CREATE TABLE location(
   location_id     VARCHAR(7) PRIMARY KEY
  ,city            VARCHAR(22)
  ,state_init      VARCHAR(2)
  ,state_full      VARCHAR(20)
  ,zipcode         VARCHAR(5)
  ,LivingCostIndex FLOAT
  ,safety          INTEGER 
);

CREATE TABLE schools_in_locations(
   id          VARCHAR(22) NOT NULL
  ,location_id VARCHAR(7) NOT NULL
  ,FOREIGN KEY (id) REFERENCES school(id)
  ,FOREIGN KEY (location_id) REFERENCES location(location_id)
);

CREATE TABLE genre(
   id       INTEGER AUTO_INCREMENT PRIMARY KEY
  ,fullname VARCHAR(20) NOT NULL
);

CREATE TABLE genres_in_schools(
   genre_id  INTEGER
  ,school_id VARCHAR(22)
  ,FOREIGN KEY (genre_id) REFERENCES genre(id)
  ,FOREIGN KEY (school_id) REFERENCES school(id) 
);

CREATE TABLE preference(
   id VARCHAR(22) PRIMARY KEY
  ,SAT  INTEGER NOT NULL
  ,high_school_GPA  FLOAT
  ,states_preference VARCHAR(2)
);

CREATE TABLE user(
   id INTEGER AUTO_INCREMENT PRIMARY KEY
  ,firstName    VARCHAR(50) NOT NULL
  ,lastName  VARCHAR(50) NOT NULL
  ,preference_id VARCHAR(22) NOT NULL
  ,email  VARCHAR(50) NOT NULL
  ,password VARCHAR(20) NOT NULL
  ,FOREIGN KEY (preference_id) REFERENCES preference(id)
);

CREATE TABLE user_backup(
                   id INTEGER AUTO_INCREMENT PRIMARY KEY
  ,firstName    VARCHAR(50) NOT NULL
  ,lastName  VARCHAR(50) NOT NULL
  ,preference_id VARCHAR(22) NOT NULL
  ,email  VARCHAR(50) NOT NULL
  ,password VARCHAR(20) NOT NULL
  ,FOREIGN KEY (preference_id) REFERENCES preference(id)
);

CREATE TABLE recommendation(
   id INTEGER AUTO_INCREMENT PRIMARY KEY 
  ,user_id INTEGER
  ,school_id VARCHAR(22) NOT NULL 
  ,matching_rate FLOAT NOT NULL 
  ,similar_school_id VARCHAR(22)
  ,FOREIGN KEY (user_id) REFERENCES user(id)
  ,FOREIGN KEY (school_id) REFERENCES school(id)
  ,FOREIGN KEY (similar_school_id) REFERENCES school(id)
);

CREATE TABLE ratings(
   school_id VARCHAR(22) NOT NULL
  ,rating    FLOAT
  ,numVotes  INTEGER
  ,FOREIGN KEY (school_id) REFERENCES school(id)
);

CREATE TABLE employees (
  email VARCHAR(50) PRIMARY KEY
  ,password VARCHAR(50) NOT NULL
  ,fullname VARCHAR(100)
);

CREATE TABLE celebrity(
  id  INTEGER AUTO_INCREMENT PRIMARY KEY
  ,name            VARCHAR(40)
  ,net_worth          INTEGER
  ,industry       VARCHAR(35)
);

CREATE TABLE celebrities_in_schools(
    celebrity_id  INTEGER
  ,school_id VARCHAR(22)
  ,FOREIGN KEY (celebrity_id) REFERENCES celebrity(id)
  ,FOREIGN KEY (school_id) REFERENCES school(id)
);
