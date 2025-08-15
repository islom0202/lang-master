package org.example.languagemaster.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizzesResults implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "quizze_id")
    @OneToOne
    private Quizzes quizzes;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private Users users;
    @Column
    @ElementCollection
    private List<String> userAnswers;
    @Column
    @ElementCollection
    private List<String> actualAnswers;
    @Column
    private Boolean isCorrect;
    @JoinColumn
    private LocalDateTime createdAt;
    @JoinColumn
    private LocalDateTime updatedAt;
}
