package org.example.languagemaster.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.example.languagemaster.Response;
import org.example.languagemaster.dto.VocabularyGroupsReq;
import org.example.languagemaster.dto.VocabularyGroupsRes;
import org.example.languagemaster.dto.VocabularyWordsRes;
import org.example.languagemaster.dto.WordsReq;
import org.example.languagemaster.entity.VocabularyGroups;
import org.example.languagemaster.service.VocabularyService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add-categories")
    @Operation(summary = "(FOR ADMIN)")
    public ResponseEntity<Response> addCategories(
            @RequestBody List<VocabularyGroupsReq> categories){
        return vocabularyService.addCategories(categories);
    }

  @PostMapping("/add-words/{categoryId}")
  @Operation(summary = "(FOR ADMIN)")
  public ResponseEntity<Response> addWords(
          @PathVariable("categoryId") Long categoryId,
          @RequestBody List<WordsReq> words) {
        return vocabularyService.addWords(categoryId, words);
    }

    @DeleteMapping("/delete/{word}")
    @Operation(summary = "(FOR ADMIN)")
    public ResponseEntity<String> deleteWord(
            @PathVariable("word") String word){
        return vocabularyService.deleteWord(word);
    }
}