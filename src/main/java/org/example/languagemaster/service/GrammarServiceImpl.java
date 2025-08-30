package org.example.languagemaster.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.languagemaster.Response;
import org.example.languagemaster.dto.GrammarReq;
import org.example.languagemaster.dto.GrammarRes;
import org.example.languagemaster.dto.TopicRes;
import org.example.languagemaster.dto.mappers.GrammarMapper;
import org.example.languagemaster.dto.mappers.UserProgressMapper;
import org.example.languagemaster.entity.GrammarTopics;
import org.example.languagemaster.entity.Levels;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.entity.enums.SectionType;
import org.example.languagemaster.repository.GrammarRepository;
import org.example.languagemaster.repository.LevelRepository;
import org.example.languagemaster.repository.UserProgressRepository;
import org.example.languagemaster.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.example.languagemaster.constraint.ApplicationMessages.GRAMMAR_TOPIC_NOT_FOUND;
import static org.example.languagemaster.constraint.ApplicationMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GrammarServiceImpl implements GrammarService {
  private final LevelRepository levelRepository;
  private final UserRepository userRepository;
  private final GrammarRepository grammarRepository;
  private final GrammarMapper grammarMapper;
  private final UserProgressRepository userProgress;
  private final UserProgressMapper progressMapper;

  @Override
  public ResponseEntity<List<Levels>> grammarLevels() {
    return ResponseEntity.of(Optional.of(levelRepository.findAll()));
  }

  @Override
  public ResponseEntity<List<GrammarRes>> lessons() {
    return ResponseEntity.ok(
        grammarRepository.findAll().stream().map(grammarMapper::mapToGrammarRes).toList());
  }

  @Override
  public ResponseEntity<Response> endLesson(Long userId, Long topicId) {
    Users user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getCode()));
    GrammarTopics topic =
        grammarRepository
            .findById(topicId)
            .orElseThrow(() -> new NoSuchElementException(GRAMMAR_TOPIC_NOT_FOUND.code));
    userProgress.save(progressMapper.buildProgress(user, topic, SectionType.GRAMMAR));
    return ResponseEntity.ok(new Response("progress_logged", true));
  }

  @Override
  @Transactional
  public ResponseEntity<List<GrammarRes>> myLessons(Long userId, Long levelId) {
    List<GrammarTopics> topics = grammarRepository.findAllByLevels_IdOrderByIdAsc(levelId);
    Set<Long> endedTopicsIds = getEndedTopicIds(userId, SectionType.GRAMMAR.toString());

    List<GrammarRes> response =
        topics.stream()
            .map(
                topic ->
                    GrammarRes.builder()
                        .id(topic.getId())
                        .title(topic.getTitle())
                        .description(topic.getDescription())
                        .example(topic.getExample())
                        .levels(topic.getLevels())
                        .assignment(topic.getAssignment())
                        .rules(topic.getRules())
                        .ended(endedTopicsIds.contains(topic.getId()))
                        .score(topic.getScore())
                        .videoUrl(topic.getVideoUrl())
                        .createdAt(topic.getCreatedAt())
                        .build())
            .toList();
    return ResponseEntity.ok(response);
  }

  private Set<Long> getEndedTopicIds(Long userId, String sectionType) {
    return new HashSet<>(
        Optional.ofNullable(userProgress.endedTopicsId(userId, sectionType))
            .orElse(Collections.emptyList()));
  }

  @Override
  public ResponseEntity<Response> addList(List<GrammarReq> request) {
    List<GrammarTopics> topics = request.stream().map(grammarMapper::mapToEntity).toList();

    grammarRepository.saveAll(topics);
    return ResponseEntity.ok(new Response("saved", true));
  }

  @Override
  public ResponseEntity<List<TopicRes>> topicList() {
    return ResponseEntity.ok(grammarRepository.getTopicList());
  }

  @Override
  public ResponseEntity<GrammarRes> lesson(Long id) {
    GrammarTopics topic =
        grammarRepository.findById(id).orElseThrow(() -> new NoSuchElementException("topic not found"));

    return ResponseEntity.ok(grammarMapper.mapToGrammarRes(topic));
  }

  @Override
  public ResponseEntity<String> delete(Long topicId) {
    grammarRepository.deleteById(topicId);
    return ResponseEntity.ok("deleted");
  }
}
