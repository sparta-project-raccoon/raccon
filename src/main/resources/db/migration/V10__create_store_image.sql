CREATE TABLE p_store_image (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    store_id    UUID                    NOT NULL,
    path        TEXT                    NOT NULL,
    is_delete   BOOLEAN  DEFAULT FALSE  NOT NULL ,
    deleted_at  TIMESTAMP,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
);
