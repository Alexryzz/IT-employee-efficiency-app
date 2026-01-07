package org.coursework.app.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.coursework.app.enums.taskEnums.TaskType;

import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stats")
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "time_effectivity")
    private Short timeEffectivity;

    @ElementCollection
    @CollectionTable(
            name = "task_type_ratios",
            joinColumns = @JoinColumn(name = "stats_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"stats_id", "task_type"}
            )
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "task_type", length = 50)
    @Column(name = "percentage", nullable = false)
    private Map<TaskType, Short> taskTypeRatio = new HashMap<>();
    @OneToOne
    @JoinColumn(name = "account_id", unique = true, nullable = false)
    private Account account;
    @PostConstruct
    public void init(){
        if (taskTypeRatio==null){
            taskTypeRatio = new HashMap<>();
            taskTypeRatio.put(TaskType.PROD_DROPPED,(short)0);
            taskTypeRatio.put(TaskType.NEW_FEATURE,(short)0);
            taskTypeRatio.put(TaskType.BAG,(short)0);
            taskTypeRatio.put(TaskType.TESTING,(short)0);
        }
    }
}
