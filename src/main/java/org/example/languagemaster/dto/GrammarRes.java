package org.example.languagemaster.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.languagemaster.entity.Levels;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrammarRes {
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private String rules;
    private String example;
    private boolean ended;
    private Levels levels;
    private String assignment;
    private int score;
    private LocalDateTime createdAt;
}
