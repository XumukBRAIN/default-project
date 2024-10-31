CREATE TABLE item
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255),
    description TEXT,
    price       NUMERIC(10, 2)
);