package org.example.languagemaster.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.languagemaster.Response;
import org.example.languagemaster.dto.AnswerQuizReq;
import org.example.languagemaster.dto.GrammarQuizeRes;
import org.example.languagemaster.dto.QuizReq;
import org.example.languagemaster.dto.QuizzesRes;
import org.example.languagemaster.dto.mappers.QuizzeMapper;
import org.example.languagemaster.entity.Quizzes;
import org.example.languagemaster.entity.QuizzesResults;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.entity.enums.SectionType;
import org.example.languagemaster.exceptionHandler.ApplicationException;
import org.example.languagemaster.repository.GrammarRepository;
import org.example.languagemaster.repository.QuizRepository;
import org.example.languagemaster.repository.QuizzesResultsRepository;
import org.example.languagemaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.example.languagemaster.constraint.ApplicationMessages.*;

@Service
@Log
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
  private final QuizRepository quizRepository;
  private final QuizzesResultsRepository quizzesResults;
  private final UserRepository userRepository;
  private final RedisCacheService cache;
  @Autowired private ExecutorService virtualThreadExecutor;
  private final QuizzeMapper quizzeMapper;
  private final GrammarRepository grammarRepository;
  private static final String QUIZE_TEMP_TABLE = "quiz_temp_table";

  @Override
  @Transactional
  public ResponseEntity<List<QuizzesRes>> quizzes(Long userId, Long topicId, String sectionType) {
    List<Long> selected = quizzesResults
            .selectedQuizzes(topicId, sectionType, userId);
    List<QuizzesRes> quizzes =
        selected.isEmpty()
            ? mapToResList(
                quizRepository.allQuizByTopicIdAndType(topicId, sectionType),
                quizzeMapper::mapToQuizzesRes)
            : mergeQuestions(userId, selected, topicId, sectionType);

    return ResponseEntity.ok(quizzes);
  }

  private List<QuizzesRes> mergeQuestions(
          Long userId, List<Long> endedQuizIds, Long topicId, String sectionType) {

    List<QuizzesRes> selected = mapToResList(
            quizzesResults.getAllByUserIdAndQuizzeId(userId, endedQuizIds),
            quizzeMapper::mapToQuizzesRes);

    List<QuizzesRes> nonSelected = mapToResList(
            quizRepository.nonSelectedQuizzes(topicId, endedQuizIds),
            quizzeMapper::mapToQuizzesRes);

    return Stream.concat(selected.stream(), nonSelected.stream())
            .sorted(Comparator.comparing(QuizzesRes::getQuizId))
            .toList();

  }
  private <T> List<QuizzesRes> mapToResList(List<T> entities, Function<T, QuizzesRes> mapper) {
    return entities.stream().map(mapper).toList();
  }

  @Override
  public void answerGrammarQuiz(AnswerQuizReq req) {
    CompletableFuture<Quizzes> quizFuture = CompletableFuture.supplyAsync(
            () -> quizRepository.findById(req.quizId())
                    .orElseThrow(() -> new NoSuchElementException(QUIZ_NOT_FOUND.getCode())),
            virtualThreadExecutor
    );

    CompletableFuture<Users> userFuture = CompletableFuture.supplyAsync(
            () -> userRepository.findById(req.userId())
                    .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getCode())),
            virtualThreadExecutor
    );

      Quizzes quiz = quizFuture.join();
      Users user = userFuture.join();
      setAnswer(user, quiz, req, checkAnswers(quiz, req));
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

  @Override
  public ResponseEntity<Response> addGrammarQuiz(Long topicId, Long score, List<QuizReq> req) {
    return buildQuiz(topicId, score, req, SectionType.GRAMMAR);
  }

  @Override
  public ResponseEntity<Response> addVocabQuiz(Long groupId, Long score, List<QuizReq> req) {
    return buildQuiz(groupId, score, req, SectionType.GRAMMAR);
  }

  private ResponseEntity<Response> buildQuiz(
      Long id, Long score, List<QuizReq> req, SectionType type) {
    List<Quizzes> quizzes =
        req.stream()
            .map(v1 -> quizzeMapper.mapToGrammarQuiz(id, score.intValue(), v1, type))
            .toList();
    quizRepository.saveAll(quizzes);
    return ResponseEntity.ok(new Response("saved", true));
  }

  private GrammarQuizeRes checkForCorrectAndIncorrects(
      Users user, Set<Long> selectedQuizId, Long grammarTopicId) {

    List<QuizzesResults> results =
        quizzesResults.getAllByUserIdAndQuizzeId(user.getId(), selectedQuizId.stream().toList());

    int totalScore = results.stream()
            .filter(QuizzesResults::getIsCorrect)
            .mapToInt(result -> result.getQuizzes().getScore())
            .sum();

    int scorePerQuiz = results.get(0).getQuizzes().getScore();
    int correctAnswersCount = totalScore / scorePerQuiz;
    return quizzeMapper.mapToRes(user, grammarTopicId, totalScore, correctAnswersCount);
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
