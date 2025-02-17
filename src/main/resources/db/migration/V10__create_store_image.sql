CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE p_store_image (
   id          uuid default uuid_generate_v4() not null constraint p_store_image_pk primary key,
   store_id    uuid                    NOT NULL,
   path        text                    NOT NULL,
   is_delete   bool default false      NOT NULL ,
   deleted_at  timestamp,
   created_at  timestamp,
   updated_at  timestamp
);