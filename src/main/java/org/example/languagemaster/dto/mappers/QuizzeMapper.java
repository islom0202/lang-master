package org.example.languagemaster.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.example.languagemaster.dto.GrammarQuizeRes;
import org.example.languagemaster.dto.QuizReq;
import org.example.languagemaster.dto.QuizzesRes;
import org.example.languagemaster.entity.GrammarTopics;
import org.example.languagemaster.entity.Quizzes;
import org.example.languagemaster.entity.QuizzesResults;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.entity.enums.SectionType;
import org.example.languagemaster.repository.GrammarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class QuizzeMapper {
  private final GrammarRepository grammarRepository;
  @Autowired private ExecutorService virtualThreadExecutor;
  private final UserProgressMapper progressMapper;

  public GrammarQuizeRes mapToRes(Users user, Long topicId, int totalScore, int correctNum) {
    GrammarTopics topic =
        grammarRepository
            .findById(topicId)
            .orElseThrow(() -> new NoSuchElementException("topic_not_found"));
    int lastScore = topic.getScore() + totalScore;

    virtualThreadExecutor.submit(
        () -> {
          progressMapper.logProgress(user, topic, totalScore, SectionType.GRAMMAR_AND_QUIZ);
        });

    return GrammarQuizeRes.builder()
        .topicId(topicId)
        .firstname(user.getFirstname())
        .lastname(user.getLastname())
        .correctCount(correctNum)
        .topicName(topic.getTitle())
        .gainedScore(lastScore)
        .build();
  }

  public Quizzes mapToGrammarQuiz(Long topicId, Integer score, QuizReq req, SectionType type) {
    return Quizzes.builder()
        .sectionId(topicId)
        .score(score)
        .sectionType(SectionType.GRAMMAR)
        .type(req.getType())
        .question(req.getQuestion())
        .correctAnswers(req.getCorrectAnswers())
        .otherAnswers(req.getOtherAnswers())
        .build();
  }

  public QuizzesRes mapToQuizzesRes(Quizzes quizzes) {
    return QuizzesRes.builder()
        .quizId(quizzes.getId())
        .question(quizzes.getQuestion())
        .sectionId(quizzes.getSectionId())
        .sectionType(quizzes.getSectionType())
        .score(quizzes.getScore())
        .userAnswers(Collections.emptyList())
        .correctAnswers(quizzes.getCorrectAnswers())
        .otherAnswers(quizzes.getOtherAnswers())
        .type(quizzes.getType())
        .build();
  }

  public QuizzesRes mapToQuizzesRes(QuizzesResults results) {
    Quizzes quizzes = results.getQuizzes();
    return QuizzesRes.builder()
        .quizId(quizzes.getId())
        .question(quizzes.getQuestion())
        .sectionId(quizzes.getSectionId())
        .sectionType(quizzes.getSectionType())
        .type(quizzes.getType())
        .score(quizzes.getScore())
        .userAnswers(results.getUserAnswers())
        .correctAnswers(quizzes.getCorrectAnswers())
        .otherAnswers(quizzes.getOtherAnswers())
        .build();
  }
}
