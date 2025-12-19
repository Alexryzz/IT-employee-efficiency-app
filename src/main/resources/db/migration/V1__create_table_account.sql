CREATE TABLE account (
                         id BIGSERIAL PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         role VARCHAR(50) NOT NULL
);

ALTER TABLE account ADD CONSTRAINT role_check
    CHECK (role IN ('WORKER', 'ADMIN'));