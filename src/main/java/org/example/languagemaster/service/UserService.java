package org.example.languagemaster.service;

import org.example.languagemaster.Response;
import org.example.languagemaster.dto.UserProfileRes;
import org.example.languagemaster.dto.UserProgressRes;
import org.example.languagemaster.dto.UserRankingRes;
import org.example.languagemaster.dto.UserRankingResDtop;
import org.example.languagemaster.entity.Levels;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
  ResponseEntity<UserProfileRes> userProfile(Long userId);

//  ResponseEntity<byte[]> profileImage(Long userId);
//
//  ResponseEntity<Response> uploadImage(Long userId, MultipartFile file);

  ResponseEntity<List<UserProgressRes>> userProgress(Long userId);

  ResponseEntity<Response> levelUp(Long userId, Levels nextLevel);

  ResponseEntity<Page<UserRankingResDtop>> ranking(
      LocalDate begin, LocalDate end, int page, int size);

  ResponseEntity<UserRankingRes> rankByUserId(Long userId);
}
