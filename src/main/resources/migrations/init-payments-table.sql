CREATE TABLE IF NOT EXISTS autopayments
(
    id                SERIAL PRIMARY KEY,
    from_account_id   INTEGER        NOT NULL,
    to_account_id     INTEGER        NOT NULL,
    amount            NUMERIC(10, 2) NOT NULL,
    start_date        DATE           NOT NULL,
    period_in_seconds INTEGER        NOT NULL,
    FOREIGN KEY (from_account_id) REFERENCES checking_account (account_id),
    FOREIGN KEY (to_account_id) REFERENCES checking_account (account_id)
);