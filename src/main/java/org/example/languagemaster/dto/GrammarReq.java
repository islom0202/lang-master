package org.example.languagemaster.dto;

import org.example.languagemaster.entity.Levels;

public record GrammarReq(
    String title,
    String description,
    String videoUrl,
    String rules,
    String example,
    Levels levels,
    String assignment,
    int score) {}
