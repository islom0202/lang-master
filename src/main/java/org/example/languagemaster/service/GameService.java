package org.example.languagemaster.service;

import lombok.RequiredArgsConstructor;
import org.example.languagemaster.dto.GameAnswers;
import org.example.languagemaster.dto.GameResult;
import org.example.languagemaster.entity.Games;
import org.example.languagemaster.repository.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final RedisCacheService cache;
    private static final String table = "game";
    public ResponseEntity<Page<Games>> gameList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Games> gamesPage = gameRepository.findAll(pageable);
        return ResponseEntity.ok(gamesPage);
    }

    public ResponseEntity<GameResult> cacheAnswers(GameAnswers answers) {
        return null;
    }
}
