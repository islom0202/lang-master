package org.example.languagemaster.service;

import org.example.languagemaster.entity.Quizzes;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizService {
    ResponseEntity<List<Quizzes>> quizzes(Long topicId, String sectionType);
}
