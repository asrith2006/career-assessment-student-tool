package com.careers.assessment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BIGINT")
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    private String testName;

    @Column(nullable = false)
    private Integer totalQuestions;

    @Column(nullable = false)
    private Integer correctAnswers;

    @Column(nullable = false)
    private Double scorePercentage;

    @Column(columnDefinition = "TEXT")
    private String answerDetails; // JSON format: [{"questionId": 1, "selectedAnswerId": 2, "isCorrect": true}]

    @Column(columnDefinition = "TEXT")
    private String recommendedCareers; // JSON format: [{"id": "1", "name": "Software Engineer", "score": 85}]

    @Column(nullable = false, updatable = false)
    private LocalDateTime completedAt;

    @Column
    private LocalDateTime reviewedAt;

    public TestResult() {
    }

    public TestResult(Long id, User user, String testName, Integer totalQuestions, Integer correctAnswers, Double scorePercentage, String answerDetails, String recommendedCareers, LocalDateTime completedAt, LocalDateTime reviewedAt) {
        this.id = id;
        this.user = user;
        this.testName = testName;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.scorePercentage = scorePercentage;
        this.answerDetails = answerDetails;
        this.recommendedCareers = recommendedCareers;
        this.completedAt = completedAt;
        this.reviewedAt = reviewedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Double getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(Double scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public String getAnswerDetails() {
        return answerDetails;
    }

    public void setAnswerDetails(String answerDetails) {
        this.answerDetails = answerDetails;
    }

    public String getRecommendedCareers() {
        return recommendedCareers;
    }

    public void setRecommendedCareers(String recommendedCareers) {
        this.recommendedCareers = recommendedCareers;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult testResult = (TestResult) o;
        return Objects.equals(id, testResult.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "id=" + id +
                ", testName='" + testName + '\'' +
                ", totalQuestions=" + totalQuestions +
                ", correctAnswers=" + correctAnswers +
                ", scorePercentage=" + scorePercentage +
                ", completedAt=" + completedAt +
                ", reviewedAt=" + reviewedAt +
                '}';
    }

    @PrePersist
    protected void onCreate() {
        completedAt = LocalDateTime.now();
    }
}
