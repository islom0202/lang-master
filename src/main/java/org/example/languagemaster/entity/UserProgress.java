package org.example.languagemaster.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.languagemaster.entity.enums.SectionType;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProgress implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Users users;
    @Column
    @Enumerated(EnumType.STRING)
    private SectionType sectionType; // 'GRAMMAR', 'VOCABULARY', 'GAME', 'VIDEO'
    @Column
    private Long sectionId;  // qaysi topic yoki vocabulary group bolsa shuni id si
    @Column
    private int score;  // game uchun har biriga 1 balldan beriladi
    @Column
    private LocalDateTime completedAt;
    @Column
    private LocalDateTime updatedAt;
}
