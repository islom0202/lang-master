package org.example.languagemaster.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.AllArgsConstructor;
import org.example.languagemaster.Response;
import org.example.languagemaster.dto.GrammarReq;
import org.example.languagemaster.dto.GrammarRes;
import org.example.languagemaster.dto.TopicRes;
import org.example.languagemaster.entity.Levels;
import org.example.languagemaster.service.GrammarService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grammar")
@AllArgsConstructor
@Validated
public class GrammarController {

    private final GrammarService grammarService;

    @GetMapping("/levels")
    @Operation(
            summary = "Get all grammar levels",
            description = "Returns a list of all grammar levels available")
    public ResponseEntity<List<Levels>> grammarLevels(){
        return grammarService.grammarLevels();
    }

    @GetMapping("/lessons")
    @Operation(
            summary = "Get all grammar lessons",
            description = "Returns a list of all grammar lessons available")
    public ResponseEntity<List<GrammarRes>> lessons(){
        return grammarService.lessons();
    }

    @GetMapping("/my-lessons")
    @Operation(
            summary = "Get all grammar lessons of a user",
            description = "Returns a list of all grammar lessons  of a user")
    public ResponseEntity<List<GrammarRes>> myLessons(
            @RequestParam Long userId,
            @RequestParam Long levelId){
        return grammarService.myLessons(userId, levelId);
    }

    @PostMapping("/end-lesson")   /*birinchi darsni korib testni ishlab keyingi dars uchun end lesson ni bosadi*/
    public ResponseEntity<Response> endLesson(
            @RequestParam Long userId,
            @RequestParam Long topicId){
        return grammarService.endLesson(userId, topicId);
    }

    @PostMapping("/add-list")
    public ResponseEntity<Response> addList(
            @RequestBody List<GrammarReq> request){
        return grammarService.addList(request);
    }

    @GetMapping("/topic-list")
    public ResponseEntity<List<TopicRes>> topicList(){
        return grammarService.topicList();
    }

}
