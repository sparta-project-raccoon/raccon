create table dev.category
(
    id   bigserial constraint category_pk primary key,
    name varchar(20) not null constraint category_unique_key unique,
    created_at  timestamp,
    updated_at  timestamp
);

insert into dev.category(name, created_at) values ('한식', now());
insert into dev.category(name, created_at) values ('중식', now());
insert into dev.category(name, created_at) values ('치킨', now());
insert into dev.category(name, created_at) values ('분식', now());
insert into dev.category(name, created_at) values ('피자', now());