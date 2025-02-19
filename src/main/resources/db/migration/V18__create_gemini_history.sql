create table dev.p_gemini_history
(
    id            uuid default uuid_generate_v4() constraint p_gemini_history_pk primary key,
    user_id       bigint      not null,
    store_id      uuid        not null,
    request_text  varchar(50) not null,
    response_text varchar(100),
    status_code   int         not null,
    created_at    timestamp,
    updated_at    timestamp
);