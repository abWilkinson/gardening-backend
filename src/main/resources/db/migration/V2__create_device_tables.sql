CREATE TABLE device (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    name VARCHAR(50),
    description VARCHAR(50),
    temperature BOOLEAN,
    humidity BOOLEAN
);
CREATE UNIQUE INDEX unq_name_user ON device (user_id, name);
CREATE INDEX user_id ON device (user_id);