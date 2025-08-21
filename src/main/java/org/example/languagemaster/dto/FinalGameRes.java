package org.example.languagemaster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinalGameRes {
    private Long userId;
    private String firstname;
    private int totalScore;
}
