package org.example.languagemaster.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.languagemaster.entity.enums.SectionType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrammarTopics implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String videoUrl;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String rules;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String example;
    @ManyToOne
    @JoinColumn(name = "level_id")
    private Levels levels;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String assignment;
    @Column
    private int score;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
}
