package com.careers.assessment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestSubmissionRequest {

    @NotNull(message = "Test name is required")
    private String testName;

    @NotEmpty(message = "Responses cannot be empty")
    private List<StudentResponse> responses;

    public TestSubmissionRequest() {
    }

    public TestSubmissionRequest(String testName, List<StudentResponse> responses) {
        this.testName = testName;
        this.responses = responses;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public List<StudentResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<StudentResponse> responses) {
        this.responses = responses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestSubmissionRequest that = (TestSubmissionRequest) o;
        return Objects.equals(testName, that.testName) && Objects.equals(responses, that.responses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testName, responses);
    }

    @Override
    public String toString() {
        return "TestSubmissionRequest{" +
                "testName='" + testName + '\'' +
                ", responses=" + responses +
                '}';
    }

    public static class StudentResponse {
        @NotNull(message = "Question ID is required")
        private Long questionId;

        @NotNull(message = "Selected answer ID is required")
        private Long selectedAnswerId;

        public StudentResponse() {
        }

        public StudentResponse(Long questionId, Long selectedAnswerId) {
            this.questionId = questionId;
            this.selectedAnswerId = selectedAnswerId;
        }

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public Long getSelectedAnswerId() {
            return selectedAnswerId;
        }

        public void setSelectedAnswerId(Long selectedAnswerId) {
            this.selectedAnswerId = selectedAnswerId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StudentResponse that = (StudentResponse) o;
            return Objects.equals(questionId, that.questionId) && Objects.equals(selectedAnswerId, that.selectedAnswerId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(questionId, selectedAnswerId);
        }

        @Override
        public String toString() {
            return "StudentResponse{" +
                    "questionId=" + questionId +
                    ", selectedAnswerId=" + selectedAnswerId +
                    '}';
        }
    }
}
