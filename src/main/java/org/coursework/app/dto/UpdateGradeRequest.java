package org.coursework.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.coursework.app.enums.WorkerGrade;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGradeRequest {
    private WorkerGrade workerGrade;
}
