-- Career Assessment System Database Schema

-- Create database
CREATE DATABASE IF NOT EXISTS career_assessment_db;
USE career_assessment_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'ADMIN') NOT NULL DEFAULT 'STUDENT',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Questions table
CREATE TABLE IF NOT EXISTS questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_text LONGTEXT NOT NULL,
    category VARCHAR(100) NOT NULL,
    description LONGTEXT,
    difficulty INT NOT NULL DEFAULT 1,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_active (active),
    INDEX idx_difficulty (difficulty)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Answers table
CREATE TABLE IF NOT EXISTS answers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    answer_text LONGTEXT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INT NOT NULL,
    career_mapping VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    INDEX idx_question (question_id),
    INDEX idx_is_correct (is_correct)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Test results table
CREATE TABLE IF NOT EXISTS test_results (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    test_name VARCHAR(255) NOT NULL,
    total_questions INT NOT NULL,
    correct_answers INT NOT NULL,
    score_percentage DECIMAL(5, 2) NOT NULL,
    answer_details LONGTEXT,
    recommended_careers LONGTEXT,
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP,
    admin_notes LONGTEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_completed (completed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Career recommendations table
CREATE TABLE IF NOT EXISTS career_recommendations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    career_name VARCHAR(255) NOT NULL UNIQUE,
    description LONGTEXT,
    required_skills VARCHAR(1000),
    salary_range VARCHAR(100),
    job_outlook VARCHAR(500),
    related_universities VARCHAR(1000),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_career_name (career_name),
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample admin user (password: admin123)
INSERT INTO users (email, password, full_name, role, active) VALUES 
('admin@assessment.com', '$2a$10$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5EQMnwk0dJ3Zm', 'Admin User', 'ADMIN', TRUE);

-- Insert sample careers
INSERT INTO career_recommendations (career_name, description, required_skills, salary_range, job_outlook) VALUES 
('Software Engineer', 'Develops and maintains computer software systems', 'Java, Python, C++, Problem Solving', '$100,000 - $150,000', 'Growing'),
('Data Scientist', 'Analyzes complex data and builds predictive models', 'Python, R, SQL, Machine Learning', '$120,000 - $180,000', 'Very Growing'),
('UI/UX Designer', 'Designs user interfaces and experiences', 'Figma, Adobe XD, Prototyping, UX Research', '$80,000 - $130,000', 'Growing'),
('Database Administrator', 'Manages and maintains databases', 'SQL, Database Design, Performance Tuning', '$90,000 - $140,000', 'Stable'),
('Cloud Architect', 'Designs scalable cloud infrastructure', 'AWS, Azure, GCP, System Design', '$130,000 - $200,000', 'Very Growing');

-- Insert sample questions
INSERT INTO questions (question_text, category, description, difficulty, active) VALUES 
('What is your primary interest?', 'General', 'Career preference question', 1, TRUE),
('How do you prefer to work?', 'Work Style', 'Work environment preference', 1, TRUE),
('What type of problems do you enjoy solving?', 'Problem Solving', 'Problem type preference', 2, TRUE),
('Which technology interests you most?', 'Technology', 'Technology preference question', 2, TRUE),
('How important is work-life balance to you?', 'Lifestyle', 'Work-life balance importance', 1, TRUE);

-- Insert sample answers for question 1
INSERT INTO answers (question_id, answer_text, is_correct, display_order, career_mapping, active) VALUES 
(1, 'Building software applications', TRUE, 1, '1', TRUE),
(1, 'Analyzing data and patterns', TRUE, 2, '2', TRUE),
(1, 'Designing user experiences', TRUE, 3, '3', TRUE),
(1, 'Managing infrastructure', TRUE, 4, '4,5', TRUE);

-- Insert sample answers for question 2
INSERT INTO answers (question_id, answer_text, is_correct, display_order, career_mapping, active) VALUES 
(2, 'Independently', TRUE, 1, '1', TRUE),
(2, 'In a team environment', TRUE, 2, '2,3', TRUE),
(2, 'Mix of both', TRUE, 3, '4,5', TRUE);

COMMIT;
