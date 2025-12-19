-- Создание таблицы stats
CREATE TABLE stats (
                       id BIGSERIAL PRIMARY KEY,
                       time_effectivity SMALLINT,
                       account_id BIGINT NOT NULL UNIQUE,

    -- Ограничения
                       CONSTRAINT fk_stats_account
                           FOREIGN KEY (account_id)
                               REFERENCES account(id)
                               ON DELETE CASCADE,

                       CONSTRAINT chk_time_effectivity
                           CHECK (time_effectivity >= 0 AND time_effectivity <= 100)
);

-- Создание таблицы для хранения маппинга task_type_ratio (ElementCollection)
CREATE TABLE task_type_mapping (
                                   stats_id BIGINT NOT NULL,
                                   task_type_key VARCHAR(50) NOT NULL,
                                   short_value SMALLINT NOT NULL,

    -- Первичный ключ
                                   PRIMARY KEY (stats_id, task_type_key),

    -- Внешний ключ
                                   CONSTRAINT fk_task_type_mapping_stats
                                       FOREIGN KEY (stats_id)
                                           REFERENCES stats(id)
                                           ON DELETE CASCADE,

    -- Ограничение для значений enum
                                   CONSTRAINT chk_task_type_key
                                       CHECK (task_type_key IN ('PROD_DROPPED', 'NEW_FEATURE', 'BAG', 'TESTING')),

    -- Ограничение для значений
                                   CONSTRAINT chk_short_value
                                       CHECK (short_value >= 0 AND short_value <= 32767) -- MAX для SMALLINT
);

-- Создание индексов для улучшения производительности
CREATE INDEX idx_stats_account_id ON stats(account_id);
CREATE INDEX idx_task_type_mapping_stats_id ON task_type_mapping(stats_id);
CREATE INDEX idx_task_type_mapping_key ON task_type_mapping(task_type_key);
