package org.example.languagemaster.service;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.languagemaster.Response;
import org.example.languagemaster.controller.AuthController;
import org.example.languagemaster.dto.SignInRequest;
import org.example.languagemaster.dto.SignUpRequest;
import org.example.languagemaster.email.EmailService;
import org.example.languagemaster.entity.Levels;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.entity.enums.UserRole;
import org.example.languagemaster.repository.LevelRepository;
import org.example.languagemaster.repository.UserRepository;
import org.example.languagemaster.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.example.languagemaster.Util.generateCode;
import static org.example.languagemaster.constraint.ApplicationMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final RedisCacheService cache;
  private final LevelRepository levelRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService email;
  private static final String USER_TABLE = "users";

  @Override
  public ResponseEntity<Response> verifyEmail(String email, String code) {
    String cachedCode = cache.get(USER_TABLE, email, new TypeReference<String>() {});
    if (cachedCode.equals(code)) {
      Users user =
          userRepository
              .findByEmail(email)
              .orElseThrow(() -> new NoSuchElementException("user_not_fount"));
      user.setVerified(true);
      userRepository.save(user);
      return ResponseEntity.ok(new Response("verified", true));
    }
    return ResponseEntity.ok(new Response("verification_failed", false));
  }

  @Override
//  @Transactional
  public ResponseEntity<Response> signUp(SignUpRequest request) {
    String key = request.email();

    ResponseEntity<Response> existCheck = checkUserIfExist(request.email());
    if (existCheck != null) {
      return existCheck;
    }

    String code = generateCode();
    try {
      userRepository.save(buildUserEntity(request));
      email.sendConfirmationCode(request.email(), code);
      cache.set(USER_TABLE, key, code, 2L, TimeUnit.MINUTES);
      return ResponseEntity.ok(new Response("code_is_sent", true));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new Response("signup_failed: " + e.getMessage(), false));
    }
  }

  private ResponseEntity<Response> checkUserIfExist(String userEmail) {
    String code = generateCode();

    if (userRepository.existsByEmail(userEmail) && !userRepository.isVerified(userEmail)) {
      String otp = cache.get(USER_TABLE, userEmail, new TypeReference<String>() {});
      if (Objects.isNull(otp)) {
        email.sendConfirmationCode(userEmail, code);
        cache.set(USER_TABLE, userEmail, code, 2L, TimeUnit.MINUTES);
        return ResponseEntity.ok(new Response("code_is_sent", true));
      }
      return ResponseEntity.ok(new Response("code_already_sent", false));
    }
    return null;
  }

  private Users buildUserEntity(SignUpRequest request) {
    Levels level = levelRepository.findById(request.levelId()).orElseThrow();
    return Users.builder()
        .firstname(request.firstname())
        .lastname(request.lastname())
        .email(request.email())
        .langLevel(level)
        .password(passwordEncoder.encode(request.password()))
        .isVerified(false)
        .role(UserRole.ROLE_USER)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  @Override
  public AuthController.Token signIn(SignInRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String accessToken = jwtUtil.generateToken(userDetails.getUsername());
    return new AuthController.Token(accessToken);
  }

  @Override
  public ResponseEntity<Response> resendCode(String userEmail) {
    Users user =
        userRepository
            .findByEmail(userEmail)
            .orElseThrow(() -> new NoSuchElementException("user_not_found"));
    if (!user.isVerified()) {
      String code = generateCode();
      email.sendConfirmationCode(userEmail, code);
      cache.set(USER_TABLE, userEmail, code, 1L, TimeUnit.MINUTES);
      return ResponseEntity.ok(new Response("new_code_is_sent_to_email", true));
    }
    return ResponseEntity.ok(new Response("already_verified", false));
  }

  @Override
  public ResponseEntity<Response> forgotPassword(String userEmail) {
    String code = generateCode();
    cache.set(USER_TABLE, userEmail, code, 2L, TimeUnit.MINUTES);
    email.sendConfirmationCode(userEmail, code);
    return ResponseEntity.ok(new Response("new_code_sent_to_email", true));
  }

  @Override
  public ResponseEntity<Response> resetPassword(String email, String newPassword) {
    Users user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getCode()));
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    return ResponseEntity.ok(new Response("new_password_set", true));
  }
}
