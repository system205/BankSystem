CREATE TABLE IF NOT EXISTS requests
(
    id             SERIAL PRIMARY KEY,
    account_number VARCHAR(255)   NOT NULL,
    amount         NUMERIC(15, 2) NOT NULL,
    type           VARCHAR(10)    NOT NULL CHECK (type IN ('withdraw', 'deposit')),
    FOREIGN KEY (account_number) REFERENCES checking_account (account_number)
);