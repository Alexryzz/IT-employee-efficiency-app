package org.coursework.app.repository;

import org.coursework.app.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
    boolean existsByTitle(String title);
    Optional<Task> findByTitle(String title);
}
