package com.careers.assessment.service;

import com.careers.assessment.dto.CareerRecommendationDTO;
import com.careers.assessment.dto.TestResultDTO;
import com.careers.assessment.dto.TestResultDTO.AnswerReviewDTO;
import com.careers.assessment.dto.TestSubmissionRequest;
import com.careers.assessment.entity.Answer;
import com.careers.assessment.entity.CareerRecommendation;
import com.careers.assessment.entity.Question;
import com.careers.assessment.entity.TestResult;
import com.careers.assessment.entity.User;
import com.careers.assessment.exception.BadRequestException;
import com.careers.assessment.exception.ResourceNotFoundException;
import com.careers.assessment.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestService {

    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CareerRecommendationRepository careerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public TestResultDTO submitTest(TestSubmissionRequest request, Authentication authentication) {
        
        // Get current user
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BadRequestException("User not found"));

        // Validate request
        if (request.getResponses() == null || request.getResponses().isEmpty()) {
            throw new BadRequestException("No responses provided");
        }

        int correctAnswers = 0;
        int totalQuestions = request.getResponses().size();
        List<AnswerReviewDTO> answerDetails = new ArrayList<>();
        Set<String> careerMappings = new HashSet<>();

        try {
            // Process each response
            for (TestSubmissionRequest.StudentResponse response : request.getResponses()) {
                Question question = questionRepository.findById(response.getQuestionId())
                        .orElseThrow(() -> new BadRequestException("Question not found: " + response.getQuestionId()));

                Answer selectedAnswer = answerRepository.findById(response.getSelectedAnswerId())
                        .orElseThrow(() -> new BadRequestException("Answer not found: " + response.getSelectedAnswerId()));

                // Check if answer is correct
                boolean isCorrect = selectedAnswer.getIsCorrect();
                if (isCorrect) {
                    correctAnswers++;
                }

                // Collect career mappings
                if (selectedAnswer.getCareerMapping() != null && !selectedAnswer.getCareerMapping().isEmpty()) {
                    careerMappings.add(selectedAnswer.getCareerMapping());
                }

                // Create answer review
                AnswerReviewDTO review = new AnswerReviewDTO();
                review.setQuestionId(question.getId());
                review.setQuestionText(question.getQuestionText());
                review.setSelectedAnswerId(selectedAnswer.getId());
                review.setSelectedAnswerText(selectedAnswer.getAnswerText());
                review.setIsCorrect(isCorrect);
                answerDetails.add(review);
            }

            // Calculate score
            double scorePercentage = (correctAnswers * 100.0) / totalQuestions;

            // Get career recommendations
            List<CareerRecommendationDTO> recommendations = getCareerRecommendations(scorePercentage, careerMappings);

            // Create and save test result
            TestResult result = new TestResult();
            result.setUser(user);
            result.setTestName(request.getTestName());
            result.setTotalQuestions(totalQuestions);
            result.setCorrectAnswers(correctAnswers);
            result.setScorePercentage(scorePercentage);
            result.setAnswerDetails(objectMapper.writeValueAsString(answerDetails));
            result.setRecommendedCareers(objectMapper.writeValueAsString(recommendations));

            TestResult savedResult = testResultRepository.save(result);

            log.info("Test submitted by user {}: Score = {}%", user.getEmail(), scorePercentage);

            return convertToTestResultDTO(savedResult, answerDetails, recommendations);

        } catch (JsonProcessingException e) {
            log.error("Error processing test submission", e);
            throw new BadRequestException("Error processing test submission");
        }
    }

    @Transactional(readOnly = true)
    public List<TestResultDTO> getUserTestResults(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BadRequestException("User not found"));

        return testResultRepository.findByUserOrderByCompletedAtDesc(user)
                .stream()
                .map(this::convertToTestResultDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TestResultDTO getTestResultById(Long resultId, Authentication authentication) {
        TestResult result = testResultRepository.findById(resultId)
                .orElseThrow(() -> new ResourceNotFoundException("TestResult", "id", resultId));

        // Verify user owns this result or is admin
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!result.getUser().getId().equals(user.getId()) && 
            user.getRole() != User.UserRole.ADMIN) {
            throw new BadRequestException("You don't have permission to view this result");
        }

        return convertToTestResultDTO(result);
    }

    @Transactional(readOnly = true)
    public List<TestResultDTO> getAllTestResults() {
        return testResultRepository.findAllByOrderByCompletedAtDesc()
                .stream()
                .map(this::convertToTestResultDTO)
                .collect(Collectors.toList());
    }

    private List<CareerRecommendationDTO> getCareerRecommendations(double scorePercentage, Set<String> careerMappings) {
        List<CareerRecommendation> allCareers = careerRepository.findByActiveTrue();
        
        // Simple recommendation algorithm based on score and career mappings
        return allCareers.stream()
                .map(career -> {
                    CareerRecommendationDTO dto = new CareerRecommendationDTO();
                    dto.setId(career.getId());
                    dto.setCareerName(career.getCareerName());
                    dto.setDescription(career.getDescription());
                    dto.setRequiredSkills(career.getRequiredSkills());
                    dto.setSalaryRange(career.getSalaryRange());
                    dto.setJobOutlook(career.getJobOutlook());
                    
                    // Calculate match percentage based on score and mappings
                    Double matchPercentage = calculateMatchPercentage(scorePercentage, careerMappings, career.getId().toString());
                    dto.setMatchPercentage(matchPercentage);
                    
                    return dto;
                })
                .filter(dto -> dto.getMatchPercentage() > 0)
                .sorted((a, b) -> b.getMatchPercentage().compareTo(a.getMatchPercentage()))
                .limit(5) // Top 5 recommendations
                .collect(Collectors.toList());
    }

    private Double calculateMatchPercentage(double scorePercentage, Set<String> careerMappings, String careerId) {
        // Simple algorithm: combine score with career mapping presence
        double baseScore = scorePercentage * 0.6; // 60% from test score
        double mappingBonus = careerMappings.contains(careerId) ? 40 : 0; // 40% if career is directly mapped
        
        return Math.min(100.0, baseScore + mappingBonus);
    }

    private TestResultDTO convertToTestResultDTO(TestResult result) {
        try {
            List<AnswerReviewDTO> answerDetails = null;
            List<CareerRecommendationDTO> recommendations = null;

            if (result.getAnswerDetails() != null) {
                answerDetails = Arrays.asList(objectMapper.readValue(result.getAnswerDetails(), AnswerReviewDTO[].class));
            }

            if (result.getRecommendedCareers() != null) {
                recommendations = Arrays.asList(objectMapper.readValue(result.getRecommendedCareers(), CareerRecommendationDTO[].class));
            }

            return convertToTestResultDTO(result, answerDetails, recommendations);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing test result", e);
            return new TestResultDTO();
        }
    }

    private TestResultDTO convertToTestResultDTO(TestResult result, 
                                                  List<AnswerReviewDTO> answerDetails,
                                                  List<CareerRecommendationDTO> recommendations) {
        TestResultDTO dto = new TestResultDTO();
        dto.setId(result.getId());
        dto.setTestName(result.getTestName());
        dto.setTotalQuestions(result.getTotalQuestions());
        dto.setCorrectAnswers(result.getCorrectAnswers());
        dto.setScorePercentage(result.getScorePercentage());
        dto.setCompletedAt(result.getCompletedAt());
        dto.setReviewedAt(result.getReviewedAt());
        dto.setAdminNotes(null); // Admin notes can be added later for review
        dto.setAnswerDetails(answerDetails);
        dto.setRecommendedCareers(recommendations);
        
        return dto;
    }
}
