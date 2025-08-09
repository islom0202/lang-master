package org.example.languagemaster.dto.mappers;

import org.example.languagemaster.dto.UserProfileRes;
import org.example.languagemaster.dto.UserProgressRes;
import org.example.languagemaster.entity.UserProgress;
import org.example.languagemaster.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public UserProfileRes mapToUserProfileRes(Users user, String imageUrl) {
    return UserProfileRes.builder()
        .fullName(user.getFirstname() + " " + user.getLastname())
        .email(user.getEmail())
        .langLevel(user.getLangLevel().getLevel())
        .imageUrl(imageUrl)
        .build();
  }

  public UserProgressRes mapToUserProgressRes(UserProgress progress){
    return UserProgressRes.builder()
            .sectionType(progress.getSectionType())
            .score(progress.getScore())
            .completedAt(progress.getCompletedAt())
            .build();
  }
}
