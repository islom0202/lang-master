package org.example.languagemaster.service;

import static org.example.languagemaster.constraint.ApplicationMessages.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import lombok.RequiredArgsConstructor;
import org.example.languagemaster.Response;
import org.example.languagemaster.dto.*;
import org.example.languagemaster.dto.mappers.UserMapper;
import org.example.languagemaster.entity.Levels;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.exceptionHandler.ApplicationException;
import org.example.languagemaster.repository.UserProgressRepository;
import org.example.languagemaster.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final UserProgressRepository progressRepository;

  @Override
//  @Transactional
  public ResponseEntity<UserProfileRes> userProfile(Long userId) {
    Users user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getCode()));
//    if (user.getImage() != null){
//      String imageUrl = buildImageUrl(user);
//      return ResponseEntity.ok(userMapper.mapToUserProfileRes(user, imageUrl));
//    }
    return ResponseEntity.ok(userMapper.mapToUserProfileRes(user, ""));
  }

//  @Override
////  @Transactional
//  public ResponseEntity<byte[]> profileImage(Long userId) {
//    Users user  = userRepository.findById(userId)
//            .orElseThrow(()-> new NoSuchElementException(USER_NOT_FOUND.getCode()));
//
//    if (user.getImage() == null) throw new NoSuchElementException(NO_IMAGE_UPLOADED.getCode());
//
//    return ResponseEntity.status(HttpStatus.OK)
//        .contentType(MediaType.valueOf("image/png"))
//        .body(user.getImage());
//    return ResponseEntity.status(HttpStatus.OK)
//        .contentType(MediaType.IMAGE_PNG)
//        .header("Content-Disposition", "inline;" + " filename=\"user-image.png\"")
//        .body(user.getImage());
//  }

//  @Override
//  public ResponseEntity<Response> uploadImage(Long userId, MultipartFile file) {
//    userRepository
//        .findById(userId)
//        .ifPresent(
//            v1 -> {
//              try {
//                v1.setImage(file.getBytes());
//                userRepository.save(v1);
//              } catch (IOException e) {
//                throw new ApplicationException(IMAGE_UPLOAD_FAILED.getCode());
//              }
//            });
//    return ResponseEntity.ok(new Response("uploaded", true));
//  }

  @Override
  public ResponseEntity<List<UserProgressRes>> userProgress(Long userId) {
    return ResponseEntity.ok(
        progressRepository.findAllByUsers_Id(userId).stream()
            .map(userMapper::mapToUserProgressRes)
            .toList());
  }

  @Override
  public ResponseEntity<Response> levelUp(Long userId, Levels nextLevel) {
    return userRepository
        .findById(userId)
        .map(
            user -> {
              user.setLangLevel(nextLevel);
              userRepository.save(user);
              return ResponseEntity.ok(new Response("updated", true));
            })
        .orElseGet(
            () ->
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(USER_NOT_FOUND.getCode(), false)));
  }

  @Override
  public ResponseEntity<Page<UserRankingResDtop>> ranking(
      LocalDate begin, LocalDate end, int page, int size) {

    Sort sort = Sort.by(Sort.Direction.DESC, "total_score");

    Pageable pageable = PageRequest.of(page, size, sort);


    LocalDateTime startOfDay = begin.atStartOfDay();
    LocalDateTime endOfDay = end.atTime(LocalTime.MAX);

    Page<UserRankingResDtop> rankings = userRepository.allRanking(startOfDay, endOfDay, pageable);

    return ResponseEntity.ok(rankings);
  }

  @Override
  public ResponseEntity<UserRankingRes> rankByUserId(Long userId) {
    Ranking rank = userRepository.getRanking(userId);
    Users user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getCode()));

    return ResponseEntity.ok(
        UserRankingRes.builder()
            .userId(userId)
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .level(user.getLangLevel().getLevel())
            .totalScore(rank.getTotalScore())
            .lessons(rank.getTotalLessons())
            .build());
  }
}
