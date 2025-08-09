package org.example.languagemaster.dto.mappers;

import org.example.languagemaster.dto.GrammarRes;
import org.example.languagemaster.entity.GrammarTopics;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

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
}
