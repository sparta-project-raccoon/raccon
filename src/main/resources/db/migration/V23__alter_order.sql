alter table dev.p_order
    alter column order_method type varchar(20) using order_method::varchar(20);

alter table dev.p_order
    alter column pay_method type varchar(20) using pay_method::varchar(20);

alter table dev.p_order
    alter column status type varchar(20) using status::varchar(20);

alter table dev.p_order
    alter column status set default 'WAIT';