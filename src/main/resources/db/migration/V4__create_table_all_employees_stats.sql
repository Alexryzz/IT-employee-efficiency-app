CREATE TABLE all_employees_stats (
                                     grade VARCHAR(20) PRIMARY KEY,
                                     count INTEGER NOT NULL DEFAULT 0
);

INSERT INTO all_employees_stats (grade, count) VALUES
                                                   ('TRAINEE',0),
                                                   ('JUNIOR', 0),
                                                   ('MIDDLE', 0),
                                                   ('SENIOR', 0),
                                                   ('LEAD', 0);

ALTER TABLE all_employees_stats
    ADD CONSTRAINT chk_all_employees_stats_grade
        CHECK (grade IN ('TRAINEE','JUNIOR', 'MIDDLE', 'SENIOR', 'LEAD'));