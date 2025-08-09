package org.example.languagemaster.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class VocabularyWords implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private VocabularyGroups groupsId;
    @Column
    private String word;
    @Column
    private String translation;
    @Column
    private String definition;
    @Column
    private String example;
    @ManyToOne
    @JoinColumn(name = "level_id")
    private Levels levels;
    @Column
    private String audioUrl;
    @Column
    private int score;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
}
