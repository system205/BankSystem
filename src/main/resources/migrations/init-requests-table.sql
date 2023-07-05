CREATE TABLE requests
(
    id             SERIAL PRIMARY KEY,
    account_number VARCHAR(255)   NOT NULL,
    amount         NUMERIC(15, 2) NOT NULL,
    request_type   VARCHAR(10)    NOT NULL CHECK (request_type IN ('withdraw', 'deposit')),
    FOREIGN KEY (account_number) REFERENCES checking_account (account_number)
);