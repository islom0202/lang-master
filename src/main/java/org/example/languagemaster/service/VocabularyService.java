package org.example.languagemaster.service;

import org.example.languagemaster.dto.VocabularyGroupsRes;
import org.example.languagemaster.dto.VocabularyWordsRes;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VocabularyService {
    ResponseEntity<List<VocabularyGroupsRes>> category();

    ResponseEntity<List<VocabularyWordsRes>> wordsByCategory(Long id);

}
