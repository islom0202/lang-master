package org.example.languagemaster.service;

import static org.example.languagemaster.constraint.ApplicationMessages.QUIZ_NOT_FOUND;
import static org.example.languagemaster.constraint.ApplicationMessages.USER_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;
import org.example.languagemaster.dto.AnswerQuizReq;
import org.example.languagemaster.entity.Quizzes;
import org.example.languagemaster.entity.QuizzesResults;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.repository.QuizRepository;
import org.example.languagemaster.repository.QuizzesResultsRepository;
import org.example.languagemaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
  private final QuizRepository quizRepository;
  private final QuizzesResultsRepository quizzesResults;
  private final UserRepository userRepository;
  @Autowired private ExecutorService virtualThreadExecutor;

  @Override
  public ResponseEntity<List<Quizzes>> quizzes(Long topicId, String sectionType) {
    List<Quizzes> quizzes = quizRepository.allQuizByTopicIdAndType(topicId, sectionType);
    return ResponseEntity.ok(quizzes);
  }

  @Override
  public void answerGrammarQuiz(AnswerQuizReq req) {
    virtualThreadExecutor.submit(
        () -> {
          Quizzes quiz =
              quizRepository
                  .findById(req.quizId())
                  .orElseThrow(() -> new NoSuchElementException(QUIZ_NOT_FOUND.getCode()));

          Users user =
              userRepository
                  .findById(req.userId())
                  .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getCode()));

          setAnswer(user, quiz, req, checkAnswers(quiz, req));
        });
  }

  private void setAnswer(Users user, Quizzes quiz, AnswerQuizReq req, boolean isCorrect) {
    quizzesResults.save(
        QuizzesResults.builder()
            .quizzes(quiz)
            .users(user)
            .userAnswers(req.selectedAnswers())
            .actualAnswers(quiz.getCorrectAnswers())
            .isCorrect(isCorrect)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build());
  }

  private boolean checkAnswers(Quizzes quiz, AnswerQuizReq req) {
    Set<String> selected = new HashSet<>(req.selectedAnswers());
    Set<String> correct = new HashSet<>(quiz.getCorrectAnswers());
    return selected.equals(correct);
  }
}
