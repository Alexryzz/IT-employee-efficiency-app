CREATE TABLE task (
                      id BIGSERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description TEXT,
                      task_type VARCHAR(50) NOT NULL,
                      task_complexity VARCHAR(50) NOT NULL,
                      task_importance VARCHAR(50) NOT NULL,
                      task_get_date TIMESTAMP,
                      deadline TIMESTAMP NOT NULL,
                      task_done_date TIMESTAMP,
                      comment TEXT,
                      task_status VARCHAR(50) NOT NULL,
                      worker_id BIGINT,
                      task_created_date TIMESTAMP NOT NULL,
                      admin_id BIGINT NOT NULL,

    -- Ограничения для валидации значений
                      CONSTRAINT chk_task_type CHECK (
                          task_type IN ('PROD_DROPPED', 'NEW_FEATURE', 'BAG', 'TESTING')
                          ),
                      CONSTRAINT chk_task_complexity CHECK (
                          task_complexity IN ('EASY', 'MEDIUM', 'HARD')
                          ),
                      CONSTRAINT chk_task_importance CHECK (
                          task_importance IN ('URGENT', 'DEFAULT', 'DEFERRED')
                          ),
                      CONSTRAINT chk_task_status CHECK (
                          task_status IN ('TO_DO', 'IN_PROGRESS', 'COMPLETED')
                          ),

    -- Ограничения для дат
                      CONSTRAINT chk_dates CHECK (
                          task_get_date IS NULL OR task_get_date >= task_created_date
                          ),
                      CONSTRAINT chk_deadline CHECK (
                          deadline >= task_created_date
                          ),
                      CONSTRAINT chk_done_date CHECK (
                          task_done_date IS NULL OR task_done_date >= task_created_date
                          ),

    -- Внешние ключи
                      CONSTRAINT fk_worker
                          FOREIGN KEY (worker_id)
                              REFERENCES account(id)
                              ON DELETE SET NULL,

                      CONSTRAINT fk_admin
                          FOREIGN KEY (admin_id)
                              REFERENCES account(id)
                              ON DELETE CASCADE
);

-- Индексы для оптимизации запросов
CREATE INDEX idx_task_worker_id ON task(worker_id);
CREATE INDEX idx_task_admin_id ON task(admin_id);
CREATE INDEX idx_task_status ON task(task_status);
CREATE INDEX idx_task_deadline ON task(deadline);
CREATE INDEX idx_task_type ON task(task_type);
CREATE INDEX idx_task_created_date ON task(task_created_date);
CREATE INDEX idx_task_complexity ON task(task_complexity);
CREATE INDEX idx_task_importance ON task(task_importance);

-- Составные индексы для частых запросов
CREATE INDEX idx_task_status_worker ON task(task_status, worker_id);
CREATE INDEX idx_task_deadline_status ON task(deadline, task_status);
CREATE INDEX idx_task_type_complexity ON task(task_type, task_complexity);