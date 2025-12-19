package org.coursework.app.repository;

import org.coursework.app.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
    boolean existsByTitle(String title);
    Task findByTitle(String title);
}
