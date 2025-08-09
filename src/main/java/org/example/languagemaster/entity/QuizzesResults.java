package org.example.languagemaster.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Table
@Data
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
    private boolean isCorrect;
}
