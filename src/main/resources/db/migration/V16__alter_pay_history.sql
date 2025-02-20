-- -- 1. 컬럼 추가
ALTER TABLE p_pay_history ADD COLUMN is_deleted BOOLEAN DEFAULT false NOT NULL;

