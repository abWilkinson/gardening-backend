CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    enabled BOOLEAN
);
CREATE UNIQUE INDEX users_email_idx ON users(email);