package org.example.languagemaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.languagemaster.entity.enums.PlanTimeType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Data
public class UserPlans implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    @Enumerated(EnumType.STRING)
    private PlanTimeType timeType;
    @Column
    @ElementCollection
    private List<LocalDateTime> targetDates;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
}
