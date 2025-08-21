package org.example.languagemaster.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.example.languagemaster.dto.FinalGameRes;
import org.example.languagemaster.dto.GameAnswers;
import org.example.languagemaster.dto.GameResult;
import org.example.languagemaster.entity.Games;
import org.example.languagemaster.entity.UserProgress;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.entity.enums.SectionType;
import org.example.languagemaster.repository.GameRepository;
import org.example.languagemaster.repository.UserProgressRepository;
import org.example.languagemaster.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.example.languagemaster.constraint.ApplicationMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GameService {
  private final UserRepository userRepository;
  private final UserProgressRepository userProgressRepository;
  private final GameRepository gameRepository;
  private final ExecutorService virtualThread;
  private final RedisCacheService cache;
  private static final String table = "game";

  public ResponseEntity<Page<Games>> gameList(int page) {
    Pageable pageable = PageRequest.of(page, 10);
    Page<Games> gamesPage = gameRepository.findAll(pageable);
    return ResponseEntity.ok(gamesPage);
  }

  public ResponseEntity<GameResult> answers(GameAnswers answers) {
    Games game =
        gameRepository
            .findById(answers.getGameId())
            .orElseThrow(() -> new NoSuchElementException("no such game"));
    boolean isCorrect =
        new HashSet<>(game.getCorrectAnswers()).containsAll(answers.getUserAnswers());

    if (isCorrect) virtualThread.submit(() -> logUserProgress(answers));

    return ResponseEntity.ok(
        GameResult.builder()
            .gameId(game.getId())
            .correctAnswers(game.getCorrectAnswers())
            .isCorrect(isCorrect)
            .build());
  }

  private void logUserProgress(GameAnswers answer) {
    Users user =
        userRepository
            .findById(answer.getUserId())
            .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getCode()));

    UserProgress progress =
        UserProgress.builder()
            .users(user)
            .sectionType(SectionType.GAME)
            .sectionId(answer.getGameId())
            .score(1)
            .completedAt(LocalDateTime.now())
            .completedAt(LocalDateTime.now())
            .build();

    cache.addToList(table, user.getId().toString(), progress, 12L, TimeUnit.MINUTES);
  }

  public ResponseEntity<FinalGameRes> finalResponse(Long userId) {
    Users user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getCode()));

    List<UserProgress> progresses =
        cache.getList(table, user.getId().toString(), new TypeReference<UserProgress>() {});
    int totalScore = progresses.stream().mapToInt(UserProgress::getScore).sum();

    virtualThread.submit(
        () ->
            userProgressRepository.save(
                UserProgress.builder()
                    .users(user)
                    .sectionType(SectionType.GAME)
                    .sectionId(null)
                    .score(totalScore)
                    .completedAt(LocalDateTime.now())
                    .completedAt(LocalDateTime.now())
                    .build()));

    return ResponseEntity.ok(
        FinalGameRes.builder()
            .firstname(user.getFirstname())
            .userId(userId)
            .totalScore(totalScore)
            .build());
  }
}
