CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table dev.p_like
(
    id         uuid default uuid_generate_v4() constraint p_like_pk primary key,
    user_id    bigint             not null,
    store_id   uuid               not null,
    is_deleted bool default false not null,
    deleted_at timestamp,
    created_at timestamp,
    updated_at timestamp
);