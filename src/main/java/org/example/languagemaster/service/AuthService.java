package org.example.languagemaster.service;

import org.example.languagemaster.Response;
import org.example.languagemaster.controller.AuthController;
import org.example.languagemaster.dto.SignInRequest;
import org.example.languagemaster.dto.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

  ResponseEntity<Response> verifyEmail(String email, String code);

  ResponseEntity<Response> signUp(SignUpRequest request);

  AuthController.Token signIn(SignInRequest request);

  ResponseEntity<Response> resendCode(String email);

  ResponseEntity<Response> forgotPassword(String email);

  ResponseEntity<Response> resetPassword(String email, String code);
}
