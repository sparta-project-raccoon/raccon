create
extension if not exists "uuid-ossp";

create table dev.p_order
(
    id           uuid default uuid_generate_v4() constraint p_order_pk primary key,
    user_id      BIGINT         NOT NULL,
    store_id     UUID           NOT NULL,
    total_price  INT NOT NULL,
    request      varchar(300),
    order_method varchar(10)    NOT NULL,
    pay_method   varchar(10)    NOT NULL,
    address      varchar(300)   NOT NULL,
    status       varchar(10)    NOT NULL,
    deleted_at   timestamp,
    created_at   timestamp,
    updated_at   timestamp
);