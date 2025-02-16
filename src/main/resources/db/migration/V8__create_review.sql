CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table dev.p_review
(
    id         uuid default uuid_generate_v4() constraint p_review_pk primary key,
    user_id    bigint         not null,
    store_id   uuid           not null,
    order_id   uuid           not null,
    content    varchar(200),
    rating     int  default 0 not null,
    is_deleted bool default false,
    deleted_at timestamp,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint
);