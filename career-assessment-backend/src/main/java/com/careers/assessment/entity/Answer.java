package com.careers.assessment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, columnDefinition = "BIGINT")
    @JsonBackReference
    private Question question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answerText;

    @Column(nullable = false)
    private Boolean isCorrect;

    @Column(nullable = false)
    private Integer displayOrder; // To maintain order of options

    // Career mapping - which career paths this answer suggests
    @Column(columnDefinition = "VARCHAR(500)")
    private String careerMapping; // JSON or comma-separated career IDs

    @Column(nullable = false)
    private Boolean active = true;

    public Answer() {
    }

    public Answer(Long id, Question question, String answerText, Boolean isCorrect, Integer displayOrder, String careerMapping, Boolean active) {
        this.id = id;
        this.question = question;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
        this.displayOrder = displayOrder;
        this.careerMapping = careerMapping;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", answerText='" + answerText + '\'' +
                ", isCorrect=" + isCorrect +
                ", displayOrder=" + displayOrder +
                ", careerMapping='" + careerMapping + '\'' +
                ", active=" + active +
                '}';
    }
}
