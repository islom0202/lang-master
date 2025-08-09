package org.example.languagemaster.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.languagemaster.Response;
import org.example.languagemaster.dto.SignInRequest;
import org.example.languagemaster.dto.SignUpRequest;
import org.example.languagemaster.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Validated
public class AuthController {
  private final AuthService authService;

  @PostMapping("/sign-in")
  @Operation(
          summary = "sign in with details")
  public ResponseEntity<Token> signIn(@Valid @RequestBody SignInRequest request) {
    return ResponseEntity.ok(authService.signIn(request));
  }

  @PostMapping("/sign-up")
  @Operation(
          summary = "register for new users")
  public ResponseEntity<Response> signUp(@Valid @RequestBody SignUpRequest request) {
    return authService.signUp(request);
  }

  @PostMapping("/verify")
  @Operation(
          summary = "verification api for otp code")
  public ResponseEntity<Response> verify(@RequestParam String email, @RequestParam String code) {
    return authService.verifyEmail(email, code);
  }

  @PostMapping("/resend-code")
  @Operation(
          summary = "resend opt code api")
  public ResponseEntity<Response> resendCode(@RequestParam String email) {
    return authService.resendCode(email);
  }

  @PostMapping("/forgot-password")
  @Operation(
          summary = "send email in param and api sends otp code to the email")
  public ResponseEntity<Response> forgotPassword(@RequestParam String email) {
    return authService.forgotPassword(email);
  }

  @PostMapping("/reset-password")
  @Operation(
          summary = "reset new password for a user")
  public ResponseEntity<Response> resetPassword(@RequestParam String email, @RequestParam String newPassword){
    return authService.resetPassword(email, newPassword);
  }

  @Data
  public static class Token {
    private String accessToken;

    public Token(String accessToken) {
      this.accessToken = accessToken;
    }
  }
}
