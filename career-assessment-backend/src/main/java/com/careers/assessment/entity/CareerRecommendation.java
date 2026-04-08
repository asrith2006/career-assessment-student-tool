package com.careers.assessment.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "career_recommendations")
public class CareerRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String careerName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requiredSkills; // JSON format

    @Column(columnDefinition = "TEXT")
    private String salaryRange;

    @Column(columnDefinition = "TEXT")
    private String jobOutlook;

    @Column(columnDefinition = "TEXT")
    private String relatedUniversities; // JSON format

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public CareerRecommendation() {
    }

    public CareerRecommendation(Long id, String careerName, String description, String requiredSkills, String salaryRange, String jobOutlook, String relatedUniversities, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CareerRecommendation that = (CareerRecommendation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CareerRecommendation{" +
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
                '}';
    }
}
