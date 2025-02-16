CREATE EXTENSION IF NOT EXISTS pgcrypto;
create extension if not exists "uuid-ossp";

create table dev.p_pay_history
(
    id         UUID default uuid_generate_v4() constraint p_pay_history_pk primary key,
    user_id        bigint   not null,
    order_id       UUID        not null,
    store_id       UUID        not null,
    payment_method VARCHAR(20) not null,
    deleted_at     timestamp,
    created_at     timestamp,
    updated_at     timestamp
);