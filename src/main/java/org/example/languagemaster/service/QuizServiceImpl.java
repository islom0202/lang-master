package org.example.languagemaster.service;

import lombok.RequiredArgsConstructor;
import org.example.languagemaster.entity.Quizzes;
import org.example.languagemaster.repository.QuizRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
  private final QuizRepository quizRepository;

  @Override
  public ResponseEntity<List<Quizzes>> quizzes(Long topicId, String sectionType) {
    List<Quizzes> quizzes = quizRepository.allQuizByTopicIdAndType(topicId, sectionType);
    return ResponseEntity.ok(quizzes);
  }
}
