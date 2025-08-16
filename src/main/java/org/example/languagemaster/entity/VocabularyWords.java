package org.example.languagemaster.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(columnDefinition = "TEXT")
    @Lob
    private String definition;
    @Column(columnDefinition = "TEXT")
    @Lob
    private String example;
    @Column
    private String level;
    @Column
    private String audioUrl;
    @Column
    private int score;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
}
