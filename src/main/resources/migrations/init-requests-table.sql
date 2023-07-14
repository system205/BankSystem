CREATE TABLE IF NOT EXISTS requests
(
    id             SERIAL PRIMARY KEY,
    account_number VARCHAR(255)             NOT NULL,
    amount         NUMERIC(15, 2)           NOT NULL,
    type           VARCHAR(10)              NOT NULL CHECK (type IN ('withdraw', 'deposit')),
    status         VARCHAR(10)              NOT NULL CHECK (status IN ('approved', 'denied', 'pending')),
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES checking_account (account_number)
);