package com.careers.assessment.controller;

import com.careers.assessment.dto.ApiResponse;
import com.careers.assessment.dto.AuthResponse;
import com.careers.assessment.dto.LoginRequest;
import com.careers.assessment.dto.RegisterRequest;
import com.careers.assessment.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request for email: {}", request.getEmail());
        
        AuthResponse response = authService.register(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Registration successful", response, HttpStatus.CREATED.value()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());
        
        AuthResponse response = authService.login(request);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response, HttpStatus.OK.value()));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<ApiResponse<Object>> validateToken() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Token is valid", HttpStatus.OK.value()));
    }
}
