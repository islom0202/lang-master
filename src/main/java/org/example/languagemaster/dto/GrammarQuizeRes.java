package org.example.languagemaster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrammarQuizeRes {
    private Long topicId;
    private String topicName;
    private String firstname;
    private String lastname;
    private int correctCount;
    private int gainedScore;
}
