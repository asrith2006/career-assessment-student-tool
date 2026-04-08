package com.careers.assessment.controller;

import com.careers.assessment.dto.ApiResponse;
import com.careers.assessment.dto.QuestionDTO;
import com.careers.assessment.service.QuestionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class QuestionController {

    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    // Public endpoints
    @GetMapping("/public/all")
    public ResponseEntity<ApiResponse<List<QuestionDTO>>> getAllActiveQuestions() {
        log.info("Fetching all active questions");
        List<QuestionDTO> questions = questionService.getAllActiveQuestions();
        return ResponseEntity.ok(new ApiResponse<>(true, "Questions retrieved successfully", questions, HttpStatus.OK.value()));
    }

    @GetMapping("/public/category/{category}")
    public ResponseEntity<ApiResponse<List<QuestionDTO>>> getQuestionsByCategory(
            @PathVariable String category) {
        log.info("Fetching questions for category: {}", category);
        List<QuestionDTO> questions = questionService.getQuestionsByCategory(category);
        return ResponseEntity.ok(new ApiResponse<>(true, "Questions retrieved successfully", questions, HttpStatus.OK.value()));
    }

    @GetMapping("/public/random")
    public ResponseEntity<ApiResponse<List<QuestionDTO>>> getRandomQuestions(
            @RequestParam(defaultValue = "10") int count) {
        log.info("Fetching {} random questions", count);
        List<QuestionDTO> questions = questionService.getRandomQuestions(count);
        return ResponseEntity.ok(new ApiResponse<>(true, "Random questions retrieved successfully", questions, HttpStatus.OK.value()));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<QuestionDTO>> getQuestionById(@PathVariable Long id) {
        log.info("Fetching question: {}", id);
        QuestionDTO question = questionService.getQuestionById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Question retrieved successfully", question, HttpStatus.OK.value()));
    }

    // Admin endpoints
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuestionDTO>> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        log.info("Creating new question");
        QuestionDTO created = questionService.createQuestion(questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Question created successfully", created, HttpStatus.CREATED.value()));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuestionDTO>> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionDTO questionDTO) {
        log.info("Updating question: {}", id);
        QuestionDTO updated = questionService.updateQuestion(id, questionDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Question updated successfully", updated, HttpStatus.OK.value()));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteQuestion(@PathVariable Long id) {
        log.info("Deactivating question: {}", id);
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Question deactivated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/admin/permanent/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> permanentlyDeleteQuestion(@PathVariable Long id) {
        log.info("Permanently deleting question: {}", id);
        questionService.permanentlyDeleteQuestion(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Question permanently deleted successfully", HttpStatus.OK.value()));
    }
}
