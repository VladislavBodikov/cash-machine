DROP TABLE IF EXISTS SCORES;
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS (
    id BIGINT not NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    passport_data VARCHAR(255),
    PRIMARY KEY ( id )
);

CREATE TABLE SCORES (
    id BIGINT not NULL,
    user_id BIGINT not NULL,
    card_number VARCHAR(255),
    score_number VARCHAR(255) not NULL,
    amount DOUBLE PRECISION not NULL,
    pin_code VARCHAR(255),
    PRIMARY KEY ( id )
);

INSERT INTO USERS (id,first_name,last_name) VALUES
  (1,'VLADISLAV','BODIKOV')
;
INSERT INTO USERS (id,first_name,last_name,passport_data) VALUES
  (2,'ALEXANDRA','SEMENOVA','empty')
;
INSERT INTO SCORES (id,user_id,card_number,score_number,amount,pin_code) VALUES
  (1,1,'40800000000000000011','2202000000000011','5115','100.50')
;