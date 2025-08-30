package org.example.languagemaster.service;

import org.example.languagemaster.Response;
import org.example.languagemaster.dto.AnswerQuizReq;
import org.example.languagemaster.dto.GrammarQuizeRes;
import org.example.languagemaster.dto.QuizReq;
import org.example.languagemaster.dto.QuizzesRes;
import org.example.languagemaster.entity.Quizzes;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizService {
    ResponseEntity<List<QuizzesRes>> quizzes(Long userId, Long topicId, String sectionType);

    void answerGrammarQuiz(AnswerQuizReq req);

    ResponseEntity<GrammarQuizeRes> getGrammarQuizeResult(Long userId, Long grammarTopiId);

    ResponseEntity<Response> addGrammarQuiz(Long topicId, Long score, List<QuizReq> req);

    ResponseEntity<Response> addVocabQuiz(Long groupId, Long score, List<QuizReq> req);

    void answerVocabQuiz(AnswerQuizReq req);

    ResponseEntity<String> delete(Long quizId);
}
