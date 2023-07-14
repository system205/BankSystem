CREATE TABLE IF NOT EXISTS checking_account
(
    account_id     SERIAL PRIMARY KEY,
    customer_id    INT                      NOT NULL,
    bank_name      VARCHAR(255)             NOT NULL,
    account_number CHAR(10) UNIQUE          NOT NULL,
    balance        DECIMAL(10, 2)           NOT NULL DEFAULT 0.00,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active         BOOLEAN                           DEFAULT true,
    FOREIGN KEY (customer_id) REFERENCES customer (id)
);