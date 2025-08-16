package org.example.languagemaster.dto.mappers;

import org.example.languagemaster.dto.GrammarReq;
import org.example.languagemaster.dto.GrammarRes;
import org.example.languagemaster.entity.GrammarTopics;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GrammarMapper {
  public GrammarRes mapToGrammarRes(GrammarTopics value) {
    return GrammarRes.builder()
        .id(value.getId())
        .title(value.getTitle())
        .description(value.getDescription())
        .example(value.getExample())
        .levels(value.getLevels())
        .assignment(value.getAssignment())
        .rules(value.getRules())
        .score(value.getScore())
        .videoUrl(value.getVideoUrl())
        .createdAt(value.getCreatedAt())
        .build();
  }

  public GrammarTopics mapToEntity(GrammarReq grammarReq) {
    return GrammarTopics.builder()
        .title(grammarReq.title())
        .description(grammarReq.description())
        .example(grammarReq.example())
        .assignment(grammarReq.assignment())
        .rules(grammarReq.rules())
        .levels(grammarReq.levels())
        .videoUrl(grammarReq.videoUrl())
        .score(grammarReq.score())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }
}
