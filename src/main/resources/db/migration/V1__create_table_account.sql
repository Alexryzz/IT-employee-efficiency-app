CREATE TABLE account (
                         id BIGSERIAL PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         role VARCHAR(50) NOT NULL,
                         grade VARCHAR(20) NULL,

                         CONSTRAINT chk_role CHECK (role IN ('ROLE_ADMIN', 'ROLE_WORKER'))
);

-- Индексы
CREATE INDEX idx_account_email ON account(email);
CREATE INDEX idx_account_role ON account(role);
CREATE INDEX idx_account_grade ON account(grade);