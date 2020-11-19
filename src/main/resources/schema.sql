
CREATE TABLE IF NOT EXISTS TODO_APP (
    TODO_ID int PRIMARY KEY,
    TITLE varchar(30) NOT NULL,
    DETAIL varchar(100) NOT NULL
);


INSERT INTO TODO_APP VALUES (1, 'Housekeeping', 'Bathroom, WC');
INSERT INTO TODO_APP VALUES (2, 'Buy present', 'Present for birthday.');