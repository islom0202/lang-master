package org.example.languagemaster.dto.mappers;

import org.example.languagemaster.entity.GrammarTopics;
import org.example.languagemaster.entity.UserProgress;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.entity.enums.SectionType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserProgressMapper {
    public UserProgress buildProgress(Users user, GrammarTopics topic){
    return UserProgress.builder()
            .users(user)
            .sectionId(topic.getId())
            .sectionType(SectionType.GRAMMAR)
            .score(topic.getScore())
            .completedAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
