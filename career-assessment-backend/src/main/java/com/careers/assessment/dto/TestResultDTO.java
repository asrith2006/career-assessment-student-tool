package com.careers.assessment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestResultDTO {
    private Long id;
    private String testName;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Double scorePercentage;
    private LocalDateTime completedAt;
    private LocalDateTime reviewedAt;
    private String adminNotes;
    private List<CareerRecommendationDTO> recommendedCareers;
    private List<AnswerReviewDTO> answerDetails;

    public TestResultDTO() {
    }

    public TestResultDTO(Long id, String testName, Integer totalQuestions, Integer correctAnswers, Double scorePercentage, LocalDateTime completedAt, LocalDateTime reviewedAt, String adminNotes, List<CareerRecommendationDTO> recommendedCareers, List<AnswerReviewDTO> answerDetails) {
        this.id = id;
        this.testName = testName;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.scorePercentage = scorePercentage;
        this.completedAt = completedAt;
        this.reviewedAt = reviewedAt;
        this.adminNotes = adminNotes;
        this.recommendedCareers = recommendedCareers;
        this.answerDetails = answerDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public List<CareerRecommendationDTO> getRecommendedCareers() {
        return recommendedCareers;
    }

    public void setRecommendedCareers(List<CareerRecommendationDTO> recommendedCareers) {
        this.recommendedCareers = recommendedCareers;
    }

    public List<AnswerReviewDTO> getAnswerDetails() {
        return answerDetails;
    }

    public void setAnswerDetails(List<AnswerReviewDTO> answerDetails) {
        this.answerDetails = answerDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResultDTO that = (TestResultDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TestResultDTO{" +
                "id=" + id +
                ", testName='" + testName + '\'' +
                ", totalQuestions=" + totalQuestions +
                ", correctAnswers=" + correctAnswers +
                ", scorePercentage=" + scorePercentage +
                ", completedAt=" + completedAt +
                ", reviewedAt=" + reviewedAt +
                ", adminNotes='" + adminNotes + '\'' +
                ", recommendedCareers=" + recommendedCareers +
                ", answerDetails=" + answerDetails +
                '}';
    }

    public static class AnswerReviewDTO {
        private Long questionId;
        private String questionText;
        private Long selectedAnswerId;
        private String selectedAnswerText;
        private Boolean isCorrect;

        public AnswerReviewDTO() {
        }

        public AnswerReviewDTO(Long questionId, String questionText, Long selectedAnswerId, String selectedAnswerText, Boolean isCorrect) {
            this.questionId = questionId;
            this.questionText = questionText;
            this.selectedAnswerId = selectedAnswerId;
            this.selectedAnswerText = selectedAnswerText;
            this.isCorrect = isCorrect;
        }

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public Long getSelectedAnswerId() {
            return selectedAnswerId;
        }

        public void setSelectedAnswerId(Long selectedAnswerId) {
            this.selectedAnswerId = selectedAnswerId;
        }

        public String getSelectedAnswerText() {
            return selectedAnswerText;
        }

        public void setSelectedAnswerText(String selectedAnswerText) {
            this.selectedAnswerText = selectedAnswerText;
        }

        public Boolean getIsCorrect() {
            return isCorrect;
        }

        public void setIsCorrect(Boolean isCorrect) {
            this.isCorrect = isCorrect;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AnswerReviewDTO that = (AnswerReviewDTO) o;
            return Objects.equals(questionId, that.questionId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(questionId);
        }

        @Override
        public String toString() {
            return "AnswerReviewDTO{" +
                    "questionId=" + questionId +
                    ", questionText='" + questionText + '\'' +
                    ", selectedAnswerId=" + selectedAnswerId +
                    ", selectedAnswerText='" + selectedAnswerText + '\'' +
                    ", isCorrect=" + isCorrect +
                    '}';
        }
    }
}
