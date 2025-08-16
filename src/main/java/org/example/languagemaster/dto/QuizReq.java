package org.example.languagemaster.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.languagemaster.entity.enums.QuestionType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizReq {
  private String question;
  private QuestionType type;
  private List<String> correctAnswers;
  private List<String> otherAnswers;
}
