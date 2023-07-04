CREATE TABLE IF NOT EXISTS roles
(
    role        VARCHAR(255) NOT NULL,
    customer_id INTEGER      NOT NULL REFERENCES customer (id),
    PRIMARY KEY (role, customer_id)
);