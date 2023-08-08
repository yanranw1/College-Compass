use collegedb;
#location
DELIMITER $$
CREATE PROCEDURE add_location (
    IN city VARCHAR(22),
    state_init VARCHAR(2),
    state_full VARCHAR(20),
    zipcode VARCHAR(5),
    LivingCostIndex FLOAT,
    safety INTEGER
)
BEGIN
  DECLARE id VARCHAR(7);

  IF LivingCostIndex = -1 THEN
    SET LivingCostIndex = 100;
END IF;

  IF safety = -1 THEN
    SET safety = 3;
END IF;

  SET id = CONCAT(state_init, zipcode);

INSERT INTO location (location_id, city, state_init, state_full, zipcode, LivingCostIndex, safety)
VALUES (id, city, state_init, state_full, zipcode, LivingCostIndex, safety);
END$$
DELIMITER ;
#genre
DELIMITER $$
CREATE PROCEDURE add_genre (
    genre_name VARCHAR(20)
)
BEGIN
    DECLARE exist INTEGER;
SELECT COUNT(*) INTO exist FROM genre WHERE fullname = genre_name;
IF exist = 0 THEN
       INSERT IGNORE INTO genre(fullname) values (genre_name);
END IF;
END$$
DELIMITER ;
#school
DELIMITER $$
CREATE PROCEDURE add_school (
    IN name VARCHAR(55),
    rating FLOAT,
    numVotes INTEGER,
    net_cost INTEGER,
    description VARCHAR(483),
    upper_SAT INTEGER,
    lower_SAT INTEGER,
    link_to_website VARCHAR(80),
    telephone VARCHAR(14),
    address VARCHAR(100),
    link_to_image VARCHAR(82),
    genre_name VARCHAR(20),
    zipcode VARCHAR(5),
    state_init VARCHAR(2),
    city VARCHAR(22)
)
BEGIN
    DECLARE exist INTEGER;
    DECLARE genre_id INTEGER;
    DECLARE location_id VARCHAR(7);
    DECLARE new_id VARCHAR(22);
    DECLARE max_id VARCHAR(22);
  DECLARE i INT DEFAULT 22;
  DECLARE cur_char CHAR(1);
  DECLARE ascii_val INT;

SELECT MAX(id) FROM school INTO max_id;

increment_loop: WHILE i > 0 DO
    SET cur_char = SUBSTRING(max_id, i, 1);
    SET ascii_val = ASCII(cur_char);

    IF ascii_val < 126 THEN
      SET ascii_val = ascii_val + 1;
      SET cur_char = CHAR(ascii_val);
      SET max_id = CONCAT(LEFT(max_id, i - 1), cur_char, RIGHT(max_id, 22 - i));
      LEAVE increment_loop;
END IF;

    SET i = i - 1;
END WHILE;



SELECT max_id INTO new_id;

INSERT INTO school(id, name, rating, numVotes, net_cost, description, upper_SAT, lower_SAT, link_to_website, telephone, address, link_to_image)
VALUES (new_id, name, rating, numVotes, net_cost, description, upper_SAT, lower_SAT, link_to_website, telephone, address, link_to_image);

SELECT COUNT(*) INTO exist FROM genre WHERE fullname = genre_name;

IF exist = 0 THEN
        INSERT INTO genre(fullname) values (genre_name);
SELECT (SELECT MAX(id)  FROM genre) INTO genre_id;
INSERT INTO genres_in_schools (genre_id, school_id) VALUES (genre_id , new_id);
ELSE
SELECT id INTO genre_id FROM genre WHERE fullname = genre_name;
INSERT INTO genres_in_schools (genre_id, school_id) VALUES (genre_id, new_id);
END IF;

SET location_id = CONCAT(state_init, zipcode);
INSERT IGNORE INTO location (location_id, city, state_init, state_full, zipcode, LivingCostIndex, safety) VALUES (location_id, city, state_init, state_init, zipcode, 108, 3);
INSERT INTO schools_in_locations(id, location_id) VALUES (new_id, location_id);

END$$
DELIMITER ;


