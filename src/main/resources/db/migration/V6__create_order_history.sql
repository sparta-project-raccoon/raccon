CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table dev.p_order_history
(
    id         UUID default uuid_generate_v4() constraint p_order_history_pk primary key,
    food_id    UUID           NOT NULL,
    store_id   UUID           NOT NULL,
    price      int            NOT NULL,
    qty        int            NOT NULL,
    is_deleted bool default false,
    deleted_at timestamp,
    created_at timestamp,
    updated_at timestamp
);