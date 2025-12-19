package org.coursework.app.entity;

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
    @Column(name = "timeEffectivity")
    private Short timeEffectivity;

    @ElementCollection
    @CollectionTable(
            name = "task_type_mapping",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @MapKeyEnumerated(EnumType.STRING) // Ключ - enum
    @MapKeyColumn(name = "task_type_key")
    @Column(name = "short_value") // Значение - Short
    private Map<TaskType, Short> taskTypeRatio = new HashMap<>();
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
    // ***
}
