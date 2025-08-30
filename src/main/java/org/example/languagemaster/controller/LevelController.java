package org.example.languagemaster.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.example.languagemaster.Response;
import org.example.languagemaster.entity.Levels;
import org.example.languagemaster.repository.LevelRepository;
import org.example.languagemaster.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/level")
@AllArgsConstructor
@Validated
public class LevelController {
  private final LevelRepository levelRepository;
  private final UserService userService;

  @PostMapping("/add")
  @Operation(summary = "(FOR ADMIN)")
  public ResponseEntity<Response> addLevel(@RequestParam String level) {
    levelRepository.save(Levels.builder().level(level).build());
    return ResponseEntity.ok(new Response("saved", true));
  }

  @GetMapping()
  public ResponseEntity<List<Levels>> levelList() {
    return ResponseEntity.ok(levelRepository.findAll());
  }

  @PutMapping("/up")
  public ResponseEntity<Response> levelUp(
          @RequestParam Long userId,
          @RequestParam Levels nextLevel){
    return userService.levelUp(userId, nextLevel);
  }
}
