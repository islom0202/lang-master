package org.example.languagemaster.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.example.languagemaster.Response;
import org.example.languagemaster.dto.UserProfileRes;
import org.example.languagemaster.dto.UserProgressRes;
import org.example.languagemaster.entity.UserProgress;
import org.example.languagemaster.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Validated
public class UserController {

  private final UserService userService;

  @GetMapping("/profile")
  @Operation(
          summary = "Get user profile",
          description = "Returns user profile")
  public ResponseEntity<UserProfileRes> userProfile(@RequestParam Long userId) {
    return userService.userProfile(userId);
  }

  @GetMapping("/profile-image/{userId}")
  @Operation(
          summary = "Get profile image",
          description = "Returns profile image as byte[]")
  public ResponseEntity<byte[]> profileImage(@PathVariable("userId") Long userId) {
    return userService.profileImage(userId);
  }

  @PostMapping("/upload-image")
  @Operation(
          summary = "Upload image for profile")
  public ResponseEntity<Response> uploadImage(
      @RequestParam("userId") Long userId, @RequestParam("file") MultipartFile file) {
      return userService.uploadImage(userId, file);
  }

  @GetMapping("/progress")
  public ResponseEntity<List<UserProgressRes>> userProgress(
          @RequestParam Long userId){
    return userService.userProgress(userId);
  }


}
