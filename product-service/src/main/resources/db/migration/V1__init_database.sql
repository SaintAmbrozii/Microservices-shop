

CREATE TABLE IF NOT EXISTS category
(
    id BIGSERIAL PRIMARY KEY,
    description varchar(255),
    name varchar(255),
    photo_uri varchar(255)
);

CREATE TABLE IF NOT EXISTS product
(
    id BIGSERIAL PRIMARY KEY,
    description varchar(255),
    name varchar(255),
    article integer,
    quantity integer,
    price numeric(8, 2),
    sale_price numeric(8,2),
    category_id bigint
        CONSTRAINT fk_category REFERENCES category
);