CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table dev.p_image
(
    id           uuid default uuid_generate_v4() constraint p_image_pk primary key,
    entity_id    uuid           NOT NULL,
    entity_type  varchar(50)    NOT NULL,
    image_url    text           NOT NULL,
    is_deleted   bool default false,
    deleted_at   timestamp,
    created_at   timestamp,
    updated_at   timestamp
);