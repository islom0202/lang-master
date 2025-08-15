package org.example.languagemaster.service;

import org.example.languagemaster.Response;
import org.example.languagemaster.dto.UserProfileRes;
import org.example.languagemaster.dto.UserProgressRes;
import org.example.languagemaster.entity.Levels;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    ResponseEntity<UserProfileRes> userProfile(Long userId);

    ResponseEntity<byte[]> profileImage(Long userId);

    ResponseEntity<Response> uploadImage(Long userId, MultipartFile file);

    ResponseEntity<List<UserProgressRes>> userProgress(Long userId);

    ResponseEntity<Response> levelUp(Long userId, Levels nextLevel);
}
