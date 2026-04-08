package com.careers.assessment.controller;

import com.careers.assessment.dto.ApiResponse;
import com.careers.assessment.dto.CareerRecommendationDTO;
import com.careers.assessment.service.CareerRecommendationService;
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
@RequestMapping("/careers")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class CareerRecommendationController {

    private static final Logger log = LoggerFactory.getLogger(CareerRecommendationController.class);

    @Autowired
    private CareerRecommendationService careerService;

    // Public endpoints
    @GetMapping("/public/all")
    public ResponseEntity<ApiResponse<List<CareerRecommendationDTO>>> getAllCareers() {
        log.info("Fetching all active careers");
        List<CareerRecommendationDTO> careers = careerService.getAllActiveCareers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Careers retrieved successfully", careers, HttpStatus.OK.value()));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<CareerRecommendationDTO>> getCareerById(@PathVariable Long id) {
        log.info("Fetching career: {}", id);
        CareerRecommendationDTO career = careerService.getCareerById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Career retrieved successfully", career, HttpStatus.OK.value()));
    }

    @GetMapping("/public/search")
    public ResponseEntity<ApiResponse<CareerRecommendationDTO>> searchCareerByName(
            @RequestParam String name) {
        log.info("Searching career by name: {}", name);
        CareerRecommendationDTO career = careerService.getCareerByName(name);
        return ResponseEntity.ok(new ApiResponse<>(true, "Career found successfully", career, HttpStatus.OK.value()));
    }

    // Admin endpoints
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CareerRecommendationDTO>> createCareer(
            @Valid @RequestBody CareerRecommendationDTO careerDTO) {
        log.info("Creating new career");
        CareerRecommendationDTO created = careerService.createCareer(careerDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Career created successfully", created, HttpStatus.CREATED.value()));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CareerRecommendationDTO>> updateCareer(
            @PathVariable Long id,
            @Valid @RequestBody CareerRecommendationDTO careerDTO) {
        log.info("Updating career: {}", id);
        CareerRecommendationDTO updated = careerService.updateCareer(id, careerDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Career updated successfully", updated, HttpStatus.OK.value()));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteCareer(@PathVariable Long id) {
        log.info("Deactivating career: {}", id);
        careerService.deleteCareer(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Career deactivated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/admin/permanent/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> permanentlyDeleteCareer(@PathVariable Long id) {
        log.info("Permanently deleting career: {}", id);
        careerService.permanentlyDeleteCareer(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Career permanently deleted successfully", HttpStatus.OK.value()));
    }
}
