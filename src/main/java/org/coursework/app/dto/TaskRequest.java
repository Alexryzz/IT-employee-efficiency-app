package org.coursework.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.coursework.app.enums.taskEnums.TaskComplexity;
import org.coursework.app.enums.taskEnums.TaskImportance;
import org.coursework.app.enums.taskEnums.TaskType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    private String title;
    private String description;
    private TaskType taskType;
    private TaskComplexity taskComplexity;
    private TaskImportance taskImportance;
    private LocalDate deadline;
}
