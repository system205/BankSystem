CREATE TABLE IF NOT EXISTS transactions
(
    id              SERIAL PRIMARY KEY,
    sender_number   CHAR(10)                 NOT NULL,
    receiver_number CHAR(10)                 NOT NULL,
    amount          DECIMAL(10, 2)           NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sender_account_number FOREIGN KEY (sender_number)
        REFERENCES checking_account (account_number),
    CONSTRAINT fk_receiver_account_number FOREIGN KEY (receiver_number)
        REFERENCES checking_account (account_number)
);
