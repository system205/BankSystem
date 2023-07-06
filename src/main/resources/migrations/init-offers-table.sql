CREATE TABLE IF NOT EXISTS offers
(
    id             SERIAL PRIMARY KEY,
    customer_email VARCHAR(255)             NOT NULL,
    status         VARCHAR(10)              NOT NULL CHECK (status IN ('pending', 'accepted', 'rejected')),
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);