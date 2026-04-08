package com.careers.assessment.controller;

import com.careers.assessment.dto.ApiResponse;
import com.careers.assessment.dto.UserDTO;
import com.careers.assessment.entity.User;
import com.careers.assessment.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        log.info("Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users, HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        log.info("Fetching user: {}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", user, HttpStatus.OK.value()));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(@PathVariable String email) {
        log.info("Fetching user by email: {}", email);
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", user, HttpStatus.OK.value()));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deactivateUser(@PathVariable Long id) {
        log.info("Deactivating user: {}", id);
        userService.deactivateUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deactivated successfully", HttpStatus.OK.value()));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> activateUser(@PathVariable Long id) {
        log.info("Activating user: {}", id);
        userService.activateUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User activated successfully", HttpStatus.OK.value()));
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserRole(
            @PathVariable Long id,
            @RequestParam User.UserRole role) {
        log.info("Updating user role: {} -> {}", id, role);
        UserDTO updatedUser = userService.updateUserRole(id, role);
        return ResponseEntity.ok(new ApiResponse<>(true, "User role updated successfully", updatedUser, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long id) {
        log.info("Deleting user: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", HttpStatus.OK.value()));
    }
}
