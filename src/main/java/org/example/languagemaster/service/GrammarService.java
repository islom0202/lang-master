package org.example.languagemaster.service;

import org.example.languagemaster.Response;
import org.example.languagemaster.dto.GrammarRes;
import org.example.languagemaster.entity.Levels;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GrammarService {
    ResponseEntity<List<Levels>> grammarLevels();

    ResponseEntity<List<GrammarRes>> lessons();

    ResponseEntity<Response> endLesson(Long userId, Long topicId);

    ResponseEntity<List<GrammarRes>> myLessons(Long userId);
}
