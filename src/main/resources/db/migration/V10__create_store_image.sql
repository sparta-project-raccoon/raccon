CREATE TABLE p_store_image (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- UUID_V4
    store_id    UUID                    NOT NULL,
    path        VARCHAR(500)            NOT NULL, -- text로 변경
    is_delete   BOOLEAN  DEFAULT FALSE  NOT NULL ,
    deleted_at  TIMESTAMP,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
);
