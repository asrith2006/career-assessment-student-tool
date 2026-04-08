package com.careers.assessment.repository;

import com.careers.assessment.entity.TestResult;
import com.careers.assessment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByUserOrderByCompletedAtDesc(User user);
    List<TestResult> findAllByOrderByCompletedAtDesc();
}
