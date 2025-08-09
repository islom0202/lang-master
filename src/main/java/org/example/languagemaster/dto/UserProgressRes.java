package org.example.languagemaster.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.entity.enums.SectionType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProgressRes {
    private Long id;
    private SectionType sectionType; // 'GRAMMAR', 'VOCABULARY', 'GAME', 'VIDEO'
    private int score;
    private LocalDateTime completedAt;
}
