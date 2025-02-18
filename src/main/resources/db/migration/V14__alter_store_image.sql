-- 1. 기존 컬럼 삭제
ALTER TABLE p_store_image DROP COLUMN is_delete;
ALTER TABLE p_store_image DROP COLUMN deleted_at;

-- 2. 이름 변경
ALTER TABLE p_store_image RENAME COLUMN is_deleted TO is_delete;
ALTER TABLE p_store_image RENAME COLUMN delete_at TO deleted_at;
