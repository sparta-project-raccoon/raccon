create table dev.p_user
(
    id                  bigserial constraint user_pk primary key,
    username            varchar(10)           not null,
    password            varchar(10)           not null,
    email               varchar(30)           not null,
    name                varchar(30)           not null,
    phone               varchar(20),
    address             varchar(300)          not null,
    role                varchar(10),
    status              varchar(10),
    is_deleted          boolean default false not null,
    deleted_at          timestamp,
    login_fail_count    int     default 0,
    password_changed_at timestamp,
    created_at          timestamp,
    updated_at          timestamp
);