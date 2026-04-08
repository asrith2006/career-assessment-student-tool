package com.careers.assessment.config;

import com.careers.assessment.entity.User;
import com.careers.assessment.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createAdminUser();
    }

    private void createAdminUser() {
        String adminEmail = "admin@assessment.com";
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Admin User");
            admin.setRole(User.UserRole.ADMIN);
            admin.setActive(true);

            userRepository.save(admin);
            log.info("Default admin created: {}", adminEmail);
        } else {
            log.info("Default admin already exists: {}", adminEmail);
        }
    }
}
