CREATE TABLE users(
    id NUMBER GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR2(50) NOT NULL,
    email VARCHAR2(50) NOT NULL,
    phone VARCHAR2(50) NOT NULL,
    PRIMARY KEY(id),
    UNIQUE (email)
    );