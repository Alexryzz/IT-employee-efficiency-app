package org.coursework.app.repository;

import jakarta.transaction.Transactional;
import org.coursework.app.entity.AllEmployeesStats;
import org.coursework.app.enums.WorkerGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AllEmployeesStatsRepository extends JpaRepository<AllEmployeesStats, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE AllEmployeesStats a SET a.count = a.count + 1 WHERE a.workerGrade = :grade")
    void increaseCountByGrade(@Param("grade") WorkerGrade workerGrade);
}
