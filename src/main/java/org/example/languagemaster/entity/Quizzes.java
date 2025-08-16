package org.example.languagemaster.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.languagemaster.entity.enums.QuestionType;
import org.example.languagemaster.entity.enums.SectionType;

import java.io.Serializable;
import java.util.List;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Quizzes implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Long sectionId; // grammar_topics.id yoki vocabulary_groups.id

  @Enumerated(EnumType.STRING)
  @Column(name = "section_type")
  private SectionType sectionType; // vocabular or grammar

  @Lob
  @Column(columnDefinition = "TEXT")
  private String question;

  @Column
  @Enumerated(EnumType.STRING)
  private QuestionType type;

  @JoinColumn
  private int score;

  @Column @ElementCollection private List<String> correctAnswers;
  @ElementCollection @Column private List<String> otherAnswers;
}
