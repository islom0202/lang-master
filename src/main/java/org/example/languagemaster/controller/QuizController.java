package org.example.languagemaster.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.example.languagemaster.dto.AnswerQuizReq;
import org.example.languagemaster.dto.GrammarQuizeRes;
import org.example.languagemaster.entity.Quizzes;
import org.example.languagemaster.entity.enums.SectionType;
import org.example.languagemaster.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@AllArgsConstructor
@Validated
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/grammar")
    @Operation(
            summary = "Get all grammar quizzes",
            description = "Returns a list of all grammar quizzes available")
    public ResponseEntity<List<Quizzes>> grammarQuizzes(
            @RequestParam Long topicId){
        return quizService.quizzes(topicId, SectionType.GRAMMAR.toString());
    }

    @GetMapping("/vocabulary")
    @Operation(
            summary = "Get all vocabulary quizzes",
            description = "Returns a list of all vocabulary quizzes available")
    public ResponseEntity<List<Quizzes>> vocabularyQuizzes(
            @RequestParam Long topicId){
        return quizService.quizzes(topicId, SectionType.VOCABULARY.toString());
    }

    @PostMapping("/grammar")
    public void answerGrammarQuiz(@RequestBody AnswerQuizReq req){
        quizService.answerGrammarQuiz(req);
    }

    @GetMapping("/grammar-result")
    public ResponseEntity<GrammarQuizeRes> getResult(
            @RequestParam Long userId,
            @RequestParam Long grammarTopiId){
        return quizService.getGrammarQuizeResult(userId, grammarTopiId);
    }
}
