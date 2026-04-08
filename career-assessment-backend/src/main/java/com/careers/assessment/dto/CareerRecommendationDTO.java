package com.careers.assessment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CareerRecommendationDTO {
    private Long id;
    private String careerName;
    private String description;
    private String requiredSkills;
    private String salaryRange;
    private String jobOutlook;
    private String relatedUniversities;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double matchPercentage; // For recommendation results

    public CareerRecommendationDTO() {
    }

    public CareerRecommendationDTO(Long id, String careerName, String description, String requiredSkills, String salaryRange, String jobOutlook, String relatedUniversities, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, Double matchPercentage) {
        this.id = id;
        this.careerName = careerName;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.salaryRange = salaryRange;
        this.jobOutlook = jobOutlook;
        this.relatedUniversities = relatedUniversities;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.matchPercentage = matchPercentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getJobOutlook() {
        return jobOutlook;
    }

    public void setJobOutlook(String jobOutlook) {
        this.jobOutlook = jobOutlook;
    }

    public String getRelatedUniversities() {
        return relatedUniversities;
    }

    public void setRelatedUniversities(String relatedUniversities) {
        this.relatedUniversities = relatedUniversities;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(Double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CareerRecommendationDTO that = (CareerRecommendationDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CareerRecommendationDTO{" +
                "id=" + id +
                ", careerName='" + careerName + '\'' +
                ", description='" + description + '\'' +
                ", requiredSkills='" + requiredSkills + '\'' +
                ", salaryRange='" + salaryRange + '\'' +
                ", jobOutlook='" + jobOutlook + '\'' +
                ", relatedUniversities='" + relatedUniversities + '\'' +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", matchPercentage=" + matchPercentage +
                '}';
    }
}
