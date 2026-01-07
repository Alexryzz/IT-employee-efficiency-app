package org.coursework.app.dto.taskDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.coursework.app.dto.AccountDto.SimpleAccountDto;
import org.coursework.app.enums.taskEnums.TaskComplexity;
import org.coursework.app.enums.taskEnums.TaskImportance;
import org.coursework.app.enums.taskEnums.TaskStatus;
import org.coursework.app.enums.taskEnums.TaskType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreatedResponse {
    private String title;
    private String description;
    private TaskType taskType;
    private TaskComplexity taskComplexity;
    private TaskImportance taskImportance;
    private LocalDateTime deadline;
    private TaskStatus taskStatus;
    private LocalDateTime taskCreatedDate;
    private SimpleAccountDto admin;
}
