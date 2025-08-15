package org.example.languagemaster.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.example.languagemaster.entity.GrammarTopics;
import org.example.languagemaster.entity.UserProgress;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.entity.enums.SectionType;
import org.example.languagemaster.repository.UserProgressRepository;
import org.example.languagemaster.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserProgressMapper {
    private final UserProgressRepository progressRepository;
    public UserProgress buildProgress(Users user, GrammarTopics topic, SectionType type){
    return UserProgress.builder()
            .users(user)
            .sectionId(topic.getId())
            .sectionType(type)
            .score(topic.getScore())
            .completedAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public void logProgress(Users user, GrammarTopics topic, int newScore, SectionType type){
        UserProgress progress = UserProgress.builder()
                .users(user)
                .sectionId(topic.getId())
                .sectionType(type)
                .score(topic.getScore())
                .completedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        progressRepository.save(progress);
    }
}
