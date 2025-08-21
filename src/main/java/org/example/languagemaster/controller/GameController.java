package org.example.languagemaster.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.example.languagemaster.dto.FinalGameRes;
import org.example.languagemaster.dto.GameAnswers;
import org.example.languagemaster.dto.GameResult;
import org.example.languagemaster.entity.Games;
import org.example.languagemaster.service.GameService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/game")
@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping("/list")
    public ResponseEntity<Page<Games>> gameList(
            @RequestParam int page){
        return gameService.gameList(page);
    }

    @PostMapping("/cache-answers")
    public ResponseEntity<GameResult> collectAnswers(
            @RequestBody GameAnswers answers){
       return gameService.answers(answers);
    }

    @GetMapping("/finish/{userId}")
    public ResponseEntity<FinalGameRes> finalResponse(
            @PathVariable("userId") Long userId){
        return gameService.finalResponse(userId);
    }

}
