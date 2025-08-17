package org.example.languagemaster.service;

import static org.example.languagemaster.Util.buildImageUrl;
import static org.example.languagemaster.constraint.ApplicationMessages.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.example.languagemaster.Response;
import org.example.languagemaster.dto.UserProfileRes;
import org.example.languagemaster.dto.UserProgressRes;
import org.example.languagemaster.dto.UserRankingRes;
import org.example.languagemaster.dto.mappers.UserMapper;
import org.example.languagemaster.entity.Levels;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.exceptionHandler.ApplicationException;
import org.example.languagemaster.repository.UserProgressRepository;
import org.example.languagemaster.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final UserProgressRepository progressRepository;

  @Override
  public ResponseEntity<UserProfileRes> userProfile(Long userId) {
    Users user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND.getCode()));
    String imageUrl = buildImageUrl(user);
    return ResponseEntity.ok(userMapper.mapToUserProfileRes(user, imageUrl));
  }

  @Override
  public ResponseEntity<byte[]> profileImage(Long userId) {
    byte[] userImage = userRepository.findById(userId).get().getImage();

    if (userImage == null) throw new NoSuchElementException(NO_IMAGE_UPLOADED.getCode());

    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_PNG)
        .header("Content-Disposition", "inline;" + " filename=\"user-image.png\"")
        .body(userImage);
  }

  @Override
  public ResponseEntity<Response> uploadImage(Long userId, MultipartFile file) {
    userRepository
        .findById(userId)
        .ifPresent(
            v1 -> {
              try {
                v1.setImage(file.getBytes());
                userRepository.save(v1);
              } catch (IOException e) {
                throw new ApplicationException(IMAGE_UPLOAD_FAILED.getCode());
              }
            });
    return ResponseEntity.ok(new Response("uploaded", true));
  }

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
  public ResponseEntity<Page<UserRankingRes>> ranking(
      LocalDate begin, LocalDate end, String orderField, String orderType, int page, int size) {
    return null;
  }

  @Override
  public ResponseEntity<UserRankingRes> rankByUserId(Long userId) {
    return null;
  }
}
