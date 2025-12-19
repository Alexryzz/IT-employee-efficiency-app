package org.coursework.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.coursework.app.enums.taskEnums.TaskComplexity;
import org.coursework.app.enums.taskEnums.TaskImportance;
import org.coursework.app.enums.taskEnums.TaskStatus;
import org.coursework.app.enums.taskEnums.TaskType;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "task_type")
    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    @Column(name = "complexity")
    @Enumerated(EnumType.STRING)
    private TaskComplexity taskComplexity;
    @Column(name = "importance")
    @Enumerated(EnumType.STRING)
    private TaskImportance taskImportance;
    @Column(name = "task_get_date")
    private LocalDate taskGetDate;
    @Column(name = "deadline")
    private LocalDate deadline;
    @Column(name = "task_done_date")
    private LocalDate taskDoneDate;
    @Column(name = "comment")
    private String comment;
    @Column(name = "task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
    @ManyToOne()
    @JoinColumn(name = "account_id")
    private Account account;
}
