package org.example.languagemaster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameResult {
    private Long gameId;
    private List<String> correctAnswers;
    private boolean isCorrect;
}
