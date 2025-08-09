package org.example.languagemaster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRes {
    private String fullName;
    private String email;
    private String langLevel;
    private String imageUrl;
}
