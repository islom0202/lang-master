package org.example.languagemaster.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.languagemaster.entity.Levels;
import org.example.languagemaster.entity.VocabularyGroups;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class VocabularyWordsRes {
    private Long id;
    private String group;
    private String word;
    private String translation;
    private String definition;
    private String example;
    private String level;
    private String audioUrl;
    private int score;
    private LocalDateTime createdAt;
}
