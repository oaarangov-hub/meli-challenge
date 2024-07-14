CREATE TABLE IF NOT EXISTS records (
    id SERIAL PRIMARY KEY,
    item_id BIGINT NOT NULL,
    site VARCHAR(3) NOT NULL,
    price DECIMAL,
    start_time VARCHAR(32),
    category_name VARCHAR(255),
    currency_description VARCHAR(255),
    seller_nickname VARCHAR(255)
);
