CREATE TABLE authors(
    id NUMBER GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR2(50) NOT NULL,
    nationality VARCHAR2(50) NOT NULL,
    PRIMARY KEY(id)
    );