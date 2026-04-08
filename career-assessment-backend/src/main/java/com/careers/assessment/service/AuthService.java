package com.careers.assessment.service;

import com.careers.assessment.dto.AuthResponse;
import com.careers.assessment.dto.LoginRequest;
import com.careers.assessment.dto.RegisterRequest;
import com.careers.assessment.entity.User;
import com.careers.assessment.exception.BadRequestException;
import com.careers.assessment.exception.ResourceNotFoundException;
import com.careers.assessment.repository.UserRepository;
import com.careers.assessment.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        
        // Validate passwords match
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(User.UserRole.STUDENT);
        user.setActive(true);

        User savedUser = userRepository.save(user);
        
        log.info("User registered successfully: {}", savedUser.getEmail());

        // Generate token
        String token = tokenProvider.generateTokenFromUsername(savedUser.getEmail());

        return new AuthResponse(
                token,
                "Bearer",
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole(),
                tokenProvider.getExpirationTime()
        );
    }

    public AuthResponse login(LoginRequest loginRequest) {
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loginRequest.getEmail()));

        if (!user.getActive()) {
            throw new BadRequestException("User account is deactivated");
        }

        // Generate token
        String token = tokenProvider.generateToken(authentication);

        log.info("User logged in successfully: {}", user.getEmail());

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                tokenProvider.getExpirationTime()
        );
    }
}
