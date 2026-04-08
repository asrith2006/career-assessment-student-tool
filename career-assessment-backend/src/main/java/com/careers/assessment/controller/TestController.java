package com.careers.assessment.controller;

import com.careers.assessment.dto.ApiResponse;
import com.careers.assessment.dto.TestResultDTO;
import com.careers.assessment.dto.TestSubmissionRequest;
import com.careers.assessment.service.TestService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;

    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<TestResultDTO>> submitTest(
            @Valid @RequestBody TestSubmissionRequest request,
            Authentication authentication) {
        
        log.info("Test submission from user: {}", authentication.getName());
        TestResultDTO result = testService.submitTest(request, authentication);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Test submitted successfully", result, HttpStatus.CREATED.value()));
    }

    @GetMapping("/my-results")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<TestResultDTO>>> getMyResults(Authentication authentication) {
        log.info("Fetching results for student: {}", authentication.getName());
        List<TestResultDTO> results = testService.getUserTestResults(authentication);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Results retrieved successfully", results, HttpStatus.OK.value()));
    }

    @GetMapping("/result/{id}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TestResultDTO>> getTestResult(
            @PathVariable Long id,
            Authentication authentication) {
        
        log.info("Fetching result: {}", id);
        TestResultDTO result = testService.getTestResultById(id, authentication);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Result retrieved successfully", result, HttpStatus.OK.value()));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TestResultDTO>>> getAllResults() {
        log.info("Fetching all test results");
        List<TestResultDTO> results = testService.getAllTestResults();
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Results retrieved successfully", results, HttpStatus.OK.value()));
    }
}
