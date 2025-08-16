package org.example.languagemaster.dto;

public record WordsReq(
    String word,
    String translation,
    String definition,
    String level,
    String example,
    String audioUrl,
    int score) {}
