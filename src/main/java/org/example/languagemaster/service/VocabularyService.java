package org.example.languagemaster.service;

import org.example.languagemaster.Response;
import org.example.languagemaster.dto.VocabularyGroupsReq;
import org.example.languagemaster.dto.VocabularyGroupsRes;
import org.example.languagemaster.dto.VocabularyWordsRes;
import org.example.languagemaster.dto.WordsReq;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VocabularyService {
    ResponseEntity<List<VocabularyGroupsRes>> category();

    ResponseEntity<List<VocabularyWordsRes>> wordsByCategory(Long id);

    ResponseEntity<Response> addCategories(List<VocabularyGroupsReq> categories);

    ResponseEntity<Response> addWords(Long categoryId, List<WordsReq> words);

    ResponseEntity<String> deleteWord(String word);
}
