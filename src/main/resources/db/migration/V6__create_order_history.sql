create extension if not exists "uuid-ossp";

create table dev.p_order_history
(
    id         UUID default uuid_generate_v4() constraint p_order_history_pk primary key,
    food_id    UUID           NOT NULL,
    store_id   UUID           NOT NULL,
    price      numeric(10, 2) NOT NULL,
    qty        int  default 1 NOT NULL,
    deleted_at timestamp,
    created_at timestamp,
    updated_at timestamp
);