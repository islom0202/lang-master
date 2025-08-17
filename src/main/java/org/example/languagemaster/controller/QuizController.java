package org.example.languagemaster.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.example.languagemaster.Response;
import org.example.languagemaster.dto.AnswerQuizReq;
import org.example.languagemaster.dto.GrammarQuizeRes;
import org.example.languagemaster.dto.QuizReq;
import org.example.languagemaster.dto.QuizzesRes;
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
    public ResponseEntity<List<QuizzesRes>> grammarQuizzes(
            @RequestParam Long topicId,
            @RequestParam Long userId){
        return quizService.quizzes(userId, topicId, SectionType.GRAMMAR.toString());
    }

    @GetMapping("/vocabulary")
    @Operation(
            summary = "Get all vocabulary quizzes",
            description = "Returns a list of all vocabulary quizzes available")
    public ResponseEntity<List<QuizzesRes>> vocabularyQuizzes(
            @RequestParam Long topicId,
            @RequestParam Long userId){
        return quizService.quizzes(userId, topicId, SectionType.VOCABULARY.toString());
    }

    @PostMapping("/grammar")
    @Operation(
            summary = "log user test answer")
    public void answerGrammarQuiz(@RequestBody AnswerQuizReq req){
        quizService.answerGrammarQuiz(req);
    }

    @PostMapping("/vocab")
    @Operation(
            summary = "log user test answer")
    public void answerVocabQuiz(@RequestBody AnswerQuizReq req){
        quizService.answerVocabQuiz(req);
    }
    @GetMapping("/grammar-result")
    @Operation(
            summary = "Get quizzes result",
            description = "Returns grammar quizzes results")
    public ResponseEntity<GrammarQuizeRes> getResult(
            @RequestParam Long userId,
            @RequestParam Long grammarTopiId){
        return quizService.getGrammarQuizeResult(userId, grammarTopiId);
    }

    @PostMapping("/add-grammar/{topicId}/{score}")
    public ResponseEntity<Response> addGrammarQuiz(
            @PathVariable("topicId") Long topicId,
            @PathVariable("score") Long score,
            @RequestBody List<QuizReq> req){
        return quizService.addGrammarQuiz(topicId, score, req);
    }

    @PostMapping("/add-vocab/{groupId}/{score}")
    public ResponseEntity<Response> addVocabQuiz(
            @PathVariable("groupId") Long groupId,
            @PathVariable("score") Long score,
            @RequestBody List<QuizReq> req){
        return quizService.addVocabQuiz(groupId, score, req);
    }
}
