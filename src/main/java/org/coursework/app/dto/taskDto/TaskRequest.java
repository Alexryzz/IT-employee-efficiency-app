package org.coursework.app.dto.taskDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.coursework.app.enums.taskEnums.TaskComplexity;
import org.coursework.app.enums.taskEnums.TaskImportance;
import org.coursework.app.enums.taskEnums.TaskType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    private String title;
    private String description;
    private TaskType taskType;
    private TaskComplexity taskComplexity;
    private TaskImportance taskImportance;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime deadline;
}
