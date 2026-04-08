package com.careers.assessment.repository;

import com.careers.assessment.entity.CareerRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRecommendationRepository extends JpaRepository<CareerRecommendation, Long> {
    List<CareerRecommendation> findByActiveTrue();
    Optional<CareerRecommendation> findByCareerName(String careerName);
}
