package com.careers.assessment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerDTO {
    private Long id;
    private String answerText;
    private Integer displayOrder;
    private String careerMapping;
    private Boolean active;
    // Exclude isCorrect from DTO responses to prevent cheating
    private Boolean isCorrect; // Only for admin/review

    public AnswerDTO() {
    }

    public AnswerDTO(Long id, String answerText, Integer displayOrder, String careerMapping, Boolean active, Boolean isCorrect) {
        this.id = id;
        this.answerText = answerText;
        this.displayOrder = displayOrder;
        this.careerMapping = careerMapping;
        this.active = active;
        this.isCorrect = isCorrect;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getCareerMapping() {
        return careerMapping;
    }

    public void setCareerMapping(String careerMapping) {
        this.careerMapping = careerMapping;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        AnswerDTO answerDTO = (AnswerDTO) o;
        return Objects.equals(id, answerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AnswerDTO{" +
                "id=" + id +
                ", answerText='" + answerText + '\'' +
                ", displayOrder=" + displayOrder +
                ", careerMapping='" + careerMapping + '\'' +
                ", active=" + active +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
