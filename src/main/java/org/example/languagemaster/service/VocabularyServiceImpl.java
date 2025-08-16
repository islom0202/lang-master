package org.example.languagemaster.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.languagemaster.Response;
import org.example.languagemaster.dto.VocabularyGroupsReq;
import org.example.languagemaster.dto.VocabularyWordsRes;
import org.example.languagemaster.dto.WordsReq;
import org.example.languagemaster.dto.mappers.MapperFactory;
import org.example.languagemaster.dto.VocabularyGroupsRes;
import org.example.languagemaster.entity.Levels;
import org.example.languagemaster.entity.VocabularyGroups;
import org.example.languagemaster.entity.VocabularyWords;
import org.example.languagemaster.repository.LevelRepository;
import org.example.languagemaster.repository.VocabularyGroupRepository;
import org.example.languagemaster.repository.VocabularyWordsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VocabularyServiceImpl implements VocabularyService {
  private final VocabularyGroupRepository vocabularyGroup;
  private final VocabularyWordsRepository wordsRepository;
  private final MapperFactory mapperFactory;

  @Override
  public ResponseEntity<List<VocabularyGroupsRes>> category() {
    var mapper = mapperFactory.getMapper(VocabularyGroups.class, VocabularyGroupsRes.class);
    return ResponseEntity.ok(vocabularyGroup.findAll().stream().map(mapper::toDto).toList());
  }

  @Override
  @Transactional
  public ResponseEntity<List<VocabularyWordsRes>> wordsByCategory(Long groupId) {
    List<VocabularyWords> entities = wordsRepository.findAllByGroupsId_Id(groupId);

    List<VocabularyWordsRes> result =
        entities.stream()
            .map(
                e ->
                    VocabularyWordsRes.builder()
                        .id(e.getId())
                        .word(e.getWord())
                        .level(e.getLevel())
                        .group(e.getGroupsId().getTitle())
                        .score(e.getScore())
                        .audioUrl(e.getAudioUrl())
                        .translation(e.getTranslation())
                        .definition(e.getDefinition())
                        .example(e.getExample())
                        .createdAt(e.getCreatedAt())
                        .build())
            .toList();

    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<Response> addCategories(List<VocabularyGroupsReq> categories) {
    List<VocabularyGroups> groups =
        categories.stream()
            .map(
                v1 ->
                    VocabularyGroups.builder()
                        .title(v1.title())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
            .toList();

    vocabularyGroup.saveAll(groups);
    return ResponseEntity.ok(new Response("saved", true));
  }

  @Override
  public ResponseEntity<Response> addWords(Long categoryId, List<WordsReq> words) {
    VocabularyGroups group =
        vocabularyGroup
            .findById(categoryId)
            .orElseThrow(() -> new NoSuchElementException("no such category"));
    List<VocabularyWords> wordsList =
        words.stream()
            .map(
                v1 ->
                    VocabularyWords.builder()
                        .word(v1.word())
                        .audioUrl(v1.audioUrl())
                        .level(v1.level())
                        .score(v1.score())
                        .definition(v1.definition())
                        .translation(v1.translation())
                        .groupsId(group)
                        .example(v1.example())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
            .toList();

    wordsRepository.saveAll(wordsList);
    return ResponseEntity.ok(new Response("saved", true));
  }
}
