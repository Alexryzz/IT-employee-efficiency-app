package org.coursework.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.coursework.app.enums.WorkerGrade;

@Entity
@Table(name = "all_employees_stats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllEmployeesStats {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private WorkerGrade workerGrade;
    @Column(name = "count")
    private Integer count;
}
