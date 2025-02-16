CREATE TABLE p_store_images (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    store_id    UUID            NOT NULL,
    path        VARCHAR(500)    NOT NULL,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,

    CONSTRAINT fk_store FOREIGN KEY (store_id) REFERENCES p_stores(id) ON DELETE CASCADE
);
