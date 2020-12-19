CREATE TABLE IF NOT EXISTS USER (
    ID BIGINT NOT NULL AUTO_INCREMENT,
    USERNAME VARCHAR(45) NOT NULL,
    PASSWORD VARCHAR(255) NOT NULL,
    ALGORITHM VARCHAR(45) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS AUTHORITY (
    ID BIGINT NOT NULL AUTO_INCREMENT,
    USER_ID BIGINT NOT NULL,
    NAME VARCHAR(45) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS PRODUCT (
    ID BIGINT NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(45) NOT NULL,
    PRICE VARCHAR(45) NOT NULL,
    CURRENCY VARCHAR(45) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE AUTHORITY
    ADD CONSTRAINT FK_AUTHORITY_USER_ID
    foreign key (USER_ID)
    references USER;