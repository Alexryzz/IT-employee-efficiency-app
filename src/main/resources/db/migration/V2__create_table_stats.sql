-- 1. Создаем таблицы
CREATE TABLE IF NOT EXISTS stats (
                                     id BIGSERIAL PRIMARY KEY,
                                     time_effectivity SMALLINT DEFAULT 0 CHECK (time_effectivity >= 0 AND time_effectivity <= 100),
                                     account_id BIGINT UNIQUE NOT NULL,
                                     FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS task_type_ratios (
                                                stats_id BIGINT NOT NULL,
                                                task_type VARCHAR(50) NOT NULL,
                                                percentage SMALLINT NOT NULL DEFAULT 0 CHECK (percentage >= 0 AND percentage <= 100),
                                                PRIMARY KEY (stats_id, task_type),
                                                FOREIGN KEY (stats_id) REFERENCES stats(id) ON DELETE CASCADE,
                                                CONSTRAINT chk_task_type CHECK (
                                                    task_type IN ('PROD_DROPPED', 'NEW_FEATURE', 'BAG', 'TESTING')
                                                    )
);

-- 2. Создаем функцию для триггера
CREATE OR REPLACE FUNCTION insert_default_task_ratios()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO task_type_ratios (stats_id, task_type, percentage)
    VALUES
        (NEW.id, 'PROD_DROPPED', 0),
        (NEW.id, 'NEW_FEATURE', 0),
        (NEW.id, 'BAG', 0),
        (NEW.id, 'TESTING', 0);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 3. Создаем триггер
DROP TRIGGER IF EXISTS trg_insert_default_task_ratios ON stats;
CREATE TRIGGER trg_insert_default_task_ratios
    AFTER INSERT ON stats
    FOR EACH ROW EXECUTE FUNCTION insert_default_task_ratios();

-- 4. Индексы
CREATE INDEX IF NOT EXISTS idx_stats_account_id ON stats(account_id);
CREATE INDEX IF NOT EXISTS idx_task_type_ratios_stats_id ON task_type_ratios(stats_id);