package org.example.languagemaster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRankingRes {
    private Long userId;
    private String firstname;
    private String lastname;
    private String level;
    private int lessons;
    private int totalScore;
}
