CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table dev.p_store_category
(
    id          uuid default uuid_generate_v4() not null constraint p_store_category_pk primary key,
    store_id    uuid                            not null,
    category_id uuid                            not null,
    is_deleted  bool default false              not null,
    deleted_at  timestamp,
    created_at  timestamp,
    updated_at  timestamp
);