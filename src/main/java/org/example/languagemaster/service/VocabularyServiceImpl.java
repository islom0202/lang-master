package org.example.languagemaster.service;

import lombok.RequiredArgsConstructor;
import org.example.languagemaster.dto.VocabularyWordsRes;
import org.example.languagemaster.dto.mappers.MapperFactory;
import org.example.languagemaster.dto.VocabularyGroupsRes;
import org.example.languagemaster.entity.VocabularyGroups;
import org.example.languagemaster.entity.VocabularyWords;
import org.example.languagemaster.repository.VocabularyGroupRepository;
import org.example.languagemaster.repository.VocabularyWordsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabularyServiceImpl implements VocabularyService{
    private final VocabularyGroupRepository vocabularyGroup;
    private final VocabularyWordsRepository wordsRepository;
    private final MapperFactory mapperFactory;
    @Override
    public ResponseEntity<List<VocabularyGroupsRes>> category() {
        var mapper = mapperFactory.getMapper(VocabularyGroups.class, VocabularyGroupsRes.class);
        return ResponseEntity.ok(
                vocabularyGroup.findAll()
                .stream()
                .map(mapper::toDto)
                .toList());
    }

    @Override
    public ResponseEntity<List<VocabularyWordsRes>> wordsByCategory(Long groupId) {
        var mapper = mapperFactory.getMapper(VocabularyWords.class, VocabularyWordsRes.class);
        return ResponseEntity.ok(
                wordsRepository
                .findAllByGroupsId_Id(groupId)
                .stream()
                .map(mapper::toDto)
                .toList());
    }
}
