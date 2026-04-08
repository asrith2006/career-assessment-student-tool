package com.careers.assessment.repository;

import com.careers.assessment.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByActiveTrue();
    List<Question> findByCategoryAndActiveTrue(String category);
    
    @Query("SELECT q FROM Question q WHERE q.active = true ORDER BY RAND() LIMIT :limit")
    List<Question> findRandomQuestions(@Param("limit") int limit);
}
