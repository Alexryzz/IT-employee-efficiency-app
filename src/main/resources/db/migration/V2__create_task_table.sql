CREATE TABLE task (
                      id BIGSERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description TEXT,
                      task_type VARCHAR(50) NOT NULL,
                      complexity VARCHAR(50) NOT NULL,
                      importance VARCHAR(50) NOT NULL,
                      task_get_date DATE,
                      deadline DATE,
                      task_done_date DATE,
                      comment TEXT,
                      task_status VARCHAR(50) NOT NULL,
                      account_id BIGINT,

                      CONSTRAINT fk_task_account
                          FOREIGN KEY (account_id)
                              REFERENCES account(id)
                              ON DELETE CASCADE,

    -- Ограничения для enum значений (согласованные с Java enum)
                      CONSTRAINT chk_task_type
                          CHECK (task_type IN ('PROD_DROPPED', 'NEW_FEATURE', 'BAG', 'TESTING')),
                      CONSTRAINT chk_complexity
                          CHECK (complexity IN ('HARD', 'MEDIUM', 'EASY')),
                      CONSTRAINT chk_importance
                          CHECK (importance IN ('URGENT', 'DEFAULT', 'DEFERRED')),
                      CONSTRAINT chk_task_status
                          CHECK (task_status IN ('TO_DO', 'IN_PROGRESS', 'COMPLETED'))
);

-- 3. Создаем индексы для таблицы task
CREATE INDEX idx_task_account_id ON task(account_id);
CREATE INDEX idx_task_status ON task(task_status);
CREATE INDEX idx_task_deadline ON task(deadline);