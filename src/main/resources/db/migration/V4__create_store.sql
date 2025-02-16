CREATE TABLE dev.p_stores (
      id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      owner_id      BIGINT          NOT NULL REFERENCES p_users(id),
      category_id   UUID            NOT NULL REFERENCES p_categories(id),

      name          VARCHAR(50)     NOT NULL,
      address       VARCHAR(300)    NOT NULL,
      status        VARCHAR(20)     NOT NULL DEFAULT 'BEFORE_OPEN',

      tel           VARCHAR(20)     NOT NULL,
      description   VARCHAR(255),

      open_time     TIME            NOT NULL,
      close_time    TIME            NOT NULL,
      closed_days   VARCHAR(20),

      created_at    TIMESTAMP,
      created_by    BIGINT,
      updated_at    TIMESTAMP,
      updated_by    BIGINT
);

