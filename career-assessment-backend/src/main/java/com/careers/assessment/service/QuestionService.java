package com.careers.assessment.service;

import com.careers.assessment.dto.AnswerDTO;
import com.careers.assessment.dto.QuestionDTO;
import com.careers.assessment.entity.Answer;
import com.careers.assessment.entity.Question;
import com.careers.assessment.exception.BadRequestException;
import com.careers.assessment.exception.ResourceNotFoundException;
import com.careers.assessment.repository.AnswerRepository;
import com.careers.assessment.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Transactional(readOnly = true)
    public List<QuestionDTO> getAllActiveQuestions() {
        return questionRepository.findByActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionsByCategory(String category) {
        return questionRepository.findByCategoryAndActiveTrue(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuestionDTO getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        return convertToDTO(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionDTO> getRandomQuestions(int count) {
        if (count <= 0 || count > 50) {
            throw new BadRequestException("Question count must be between 1 and 50");
        }
        return questionRepository.findRandomQuestions(count)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setQuestionText(questionDTO.getQuestionText());
        question.setCategory(questionDTO.getCategory());
        question.setDescription(questionDTO.getDescription());
        question.setDifficulty(questionDTO.getDifficulty());
        question.setActive(true);

        Question savedQuestion = questionRepository.save(question);
        
        // Add answers if provided
        if (questionDTO.getAnswers() != null) {
            for (AnswerDTO answerDTO : questionDTO.getAnswers()) {
                Answer answer = new Answer();
                answer.setQuestion(savedQuestion);
                answer.setAnswerText(answerDTO.getAnswerText());
                answer.setIsCorrect(answerDTO.getIsCorrect());
                answer.setDisplayOrder(answerDTO.getDisplayOrder());
                answer.setCareerMapping(answerDTO.getCareerMapping());
                answer.setActive(true);
                answerRepository.save(answer);
            }
            // Refresh to get updated answers
            savedQuestion = questionRepository.findById(savedQuestion.getId()).get();
        }

        log.info("Question created: {}", savedQuestion.getId());
        return convertToDTO(savedQuestion);
    }

    @Transactional
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));

        question.setQuestionText(questionDTO.getQuestionText());
        question.setCategory(questionDTO.getCategory());
        question.setDescription(questionDTO.getDescription());
        question.setDifficulty(questionDTO.getDifficulty());

        Question updatedQuestion = questionRepository.save(question);
        
        log.info("Question updated: {}", id);
        return convertToDTO(updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        
        question.setActive(false);
        questionRepository.save(question);
        
        log.info("Question deactivated: {}", id);
    }

    @Transactional
    public void permanentlyDeleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question", "id", id);
        }
        questionRepository.deleteById(id);
        log.info("Question permanently deleted: {}", id);
    }

    private QuestionDTO convertToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setCategory(question.getCategory());
        dto.setDescription(question.getDescription());
        dto.setDifficulty(question.getDifficulty());
        dto.setActive(question.getActive());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setUpdatedAt(question.getUpdatedAt());

        // Don't include isCorrect for students (prevents cheating)
        List<AnswerDTO> answers = question.getAnswers().stream()
                .map(answer -> new AnswerDTO(
                        answer.getId(),
                        answer.getAnswerText(),
                        answer.getDisplayOrder(),
                        answer.getCareerMapping(),
                        answer.getActive(),
                        null // Don't expose isCorrect to frontend
                ))
                .collect(Collectors.toList());
        
        dto.setAnswers(answers);
        return dto;
    }
}
