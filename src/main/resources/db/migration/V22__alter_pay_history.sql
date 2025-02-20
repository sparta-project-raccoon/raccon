ALTER TABLE p_pay_history ADD COLUMN status varchar(10) DEFAULT 'READY' NOT NULL;
ALTER TABLE p_pay_history DROP COLUMN pay_method;