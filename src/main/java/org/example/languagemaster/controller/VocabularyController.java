package org.example.languagemaster.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.example.languagemaster.dto.VocabularyGroupsRes;
import org.example.languagemaster.dto.VocabularyWordsRes;
import org.example.languagemaster.entity.VocabularyGroups;
import org.example.languagemaster.service.VocabularyService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vocabulary")
@AllArgsConstructor
@Validated
public class VocabularyController {
    private final VocabularyService vocabularyService;

    @GetMapping("/category")
    @Operation(
            summary = "Get a list of categories of the words",
            description = "Returns a list of categories in dto")
    public ResponseEntity<List<VocabularyGroupsRes>> category(){
        return vocabularyService.category();
    }

    @GetMapping("/words")
    @Operation(
            summary = "Get a list of words",
            description = "Returns a list of words related to the category")
    public ResponseEntity<List<VocabularyWordsRes>> wordsByCategory(
            @RequestParam Long groupId){
        return vocabularyService.wordsByCategory(groupId);
    }
}