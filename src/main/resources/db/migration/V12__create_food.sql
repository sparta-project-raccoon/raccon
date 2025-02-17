CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE dev.p_food
(
    food_id     uuid      DEFAULT uuid_generate_v4() CONSTRAINT p_food_pk PRIMARY KEY,
    store_id    uuid        NOT NULL,
    name        varchar(30) NOT NULL,
    price       bigint      NOT NULL,
    description varchar(300),
    image_path  text        NOT NULL,
    status      varchar(30),
    is_deleted  boolean   DEFAULT false,
    created_at  timestamp DEFAULT now(),
    updated_at  timestamp DEFAULT now()
);