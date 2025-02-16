create extension if not exists "uuid-ossp";

create table dev."orders"
(
    id UUID PRIMARY KEY uuid_generate_v4(),
    user_id             bigserial        not null,
    store_id            uuid uuid_generate_v4 ()        not null,
    total_price         numeric(10,2)         not null,
    order_method        varchar(10)           not null,
    request             varchar(300),
    pay_method          varchar(10)           not null,
    address             varchar(300)          not null,
    role                varchar(10),
    status              varchar(10),
    deleted_at          timestamp,
    created_at          timestamp,
    updated_at          timestamp
);