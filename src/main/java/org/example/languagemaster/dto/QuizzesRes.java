package org.example.languagemaster.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.languagemaster.entity.enums.QuestionType;
import org.example.languagemaster.entity.enums.SectionType;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizzesRes {
    private Long quizId;
    private Long sectionId; // grammar_topics.id yoki vocabulary_groups.id
    private SectionType sectionType; // vocabular or grammar
    private String question;
    private QuestionType type;
    private int score;
    private List<String> correctAnswers;
    private List<String> otherAnswers;
    private List<String> userAnswers;
}
