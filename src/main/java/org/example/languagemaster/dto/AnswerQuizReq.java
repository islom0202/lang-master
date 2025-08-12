package org.example.languagemaster.dto;

import java.util.List;

public record AnswerQuizReq(
        Long quizId,
        Long userId,
        List<String> selectedAnswers
) {}
