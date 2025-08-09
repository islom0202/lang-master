package org.example.languagemaster.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "firstname_is_required")
        String firstname,
        @NotBlank(message = "lastname_is_required")
        String lastname,
        @NotBlank(message = "email_is_required")
        @Size(min = 6, message = "email_is_not_valid")
        String email,
        @NotBlank(message = "password_is_required")
        @Size(min = 4, message = "password_is_not_valid")
        String password,
        Long levelId
) {}
