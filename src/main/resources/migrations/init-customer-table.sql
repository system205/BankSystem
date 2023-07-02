CREATE TABLE IF NOT EXISTS customer
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    surname  VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);