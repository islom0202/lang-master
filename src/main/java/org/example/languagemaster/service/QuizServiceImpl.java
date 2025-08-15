package org.example.languagemaster.service;

import static org.example.languagemaster.constraint.ApplicationMessages.QUIZ_NOT_FOUND;
import static org.example.languagemaster.constraint.ApplicationMessages.USER_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.example.languagemaster.dto.AnswerQuizReq;
import org.example.languagemaster.dto.GrammarQuizeRes;
import org.example.languagemaster.dto.mappers.QuizzeMapper;
import org.example.languagemaster.entity.Quizzes;
import org.example.languagemaster.entity.QuizzesResults;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.exceptionHandler.ApplicationException;
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
  private final RedisCacheService cache;
  @Autowired private ExecutorService virtualThreadExecutor;
  private final QuizzeMapper quizzeMapper;
  private static final String QUIZE_TEMP_TABLE = "quiz_temp_table";

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

  @Override
  public ResponseEntity<GrammarQuizeRes> getGrammarQuizeResult(Long userId, Long grammarTopiId) {
    Users user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getCode()));
    Set<Long> selectedQuizId =
        cache.getSet(QUIZE_TEMP_TABLE, user.getEmail(), new TypeReference<Long>() {});

    if (selectedQuizId == null || selectedQuizId.isEmpty())
      throw new ApplicationException("you_have_answered_questions");

    return ResponseEntity.ok(checkForCorrectAndIncorrects(user, selectedQuizId, grammarTopiId));
  }

  private GrammarQuizeRes checkForCorrectAndIncorrects(
          Users user, Set<Long> selectedQuizId, Long grammarTopicId) {

    List<QuizzesResults> results =
            quizzesResults.getAllByUserIdAndQuizzeId(user.getId(), selectedQuizId);

    long countCorrects = results.stream()
            .filter(QuizzesResults::getIsCorrect)
            .count();

    return quizzeMapper.mapToRes(user, grammarTopicId, (int) countCorrects);
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
    cache.addToSet(QUIZE_TEMP_TABLE, user.getEmail(), quiz.getId(), 17L, TimeUnit.MINUTES);
  }

  private boolean checkAnswers(Quizzes quiz, AnswerQuizReq req) {
    Set<String> selected = new HashSet<>(req.selectedAnswers());
    Set<String> correct = new HashSet<>(quiz.getCorrectAnswers());
    return selected.equals(correct);
  }
}
