package org.coursework.app.repository;

import org.coursework.app.entity.Stats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepository extends JpaRepository<Stats, Long> {
    boolean existsByAccountEmail(String email);
}
