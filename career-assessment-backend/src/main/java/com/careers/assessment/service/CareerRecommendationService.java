package com.careers.assessment.service;

import com.careers.assessment.dto.CareerRecommendationDTO;
import com.careers.assessment.entity.CareerRecommendation;
import com.careers.assessment.exception.BadRequestException;
import com.careers.assessment.exception.ResourceNotFoundException;
import com.careers.assessment.repository.CareerRecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareerRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(CareerRecommendationService.class);

    @Autowired
    private CareerRecommendationRepository careerRepository;

    @Transactional(readOnly = true)
    public List<CareerRecommendationDTO> getAllActiveCareers() {
        return careerRepository.findByActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CareerRecommendationDTO getCareerById(Long id) {
        CareerRecommendation career = careerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Career", "id", id));
        return convertToDTO(career);
    }

    @Transactional(readOnly = true)
    public CareerRecommendationDTO getCareerByName(String name) {
        CareerRecommendation career = careerRepository.findByCareerName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Career", "name", name));
        return convertToDTO(career);
    }

    @Transactional
    public CareerRecommendationDTO createCareer(CareerRecommendationDTO careerDTO) {
        // Check if career already exists
        if (careerRepository.findByCareerName(careerDTO.getCareerName()).isPresent()) {
            throw new BadRequestException("Career already exists: " + careerDTO.getCareerName());
        }

        CareerRecommendation career = new CareerRecommendation();
        career.setCareerName(careerDTO.getCareerName());
        career.setDescription(careerDTO.getDescription());
        career.setRequiredSkills(careerDTO.getRequiredSkills());
        career.setSalaryRange(careerDTO.getSalaryRange());
        career.setJobOutlook(careerDTO.getJobOutlook());
        career.setRelatedUniversities(careerDTO.getRelatedUniversities());
        career.setActive(true);

        CareerRecommendation savedCareer = careerRepository.save(career);
        log.info("Career created: {}", savedCareer.getId());
        
        return convertToDTO(savedCareer);
    }

    @Transactional
    public CareerRecommendationDTO updateCareer(Long id, CareerRecommendationDTO careerDTO) {
        CareerRecommendation career = careerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Career", "id", id));

        career.setCareerName(careerDTO.getCareerName());
        career.setDescription(careerDTO.getDescription());
        career.setRequiredSkills(careerDTO.getRequiredSkills());
        career.setSalaryRange(careerDTO.getSalaryRange());
        career.setJobOutlook(careerDTO.getJobOutlook());
        career.setRelatedUniversities(careerDTO.getRelatedUniversities());

        CareerRecommendation updatedCareer = careerRepository.save(career);
        log.info("Career updated: {}", id);
        
        return convertToDTO(updatedCareer);
    }

    @Transactional
    public void deleteCareer(Long id) {
        CareerRecommendation career = careerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Career", "id", id));
        
        career.setActive(false);
        careerRepository.save(career);
        log.info("Career deactivated: {}", id);
    }

    @Transactional
    public void permanentlyDeleteCareer(Long id) {
        if (!careerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Career", "id", id);
        }
        careerRepository.deleteById(id);
        log.info("Career permanently deleted: {}", id);
    }

    private CareerRecommendationDTO convertToDTO(CareerRecommendation career) {
        return new CareerRecommendationDTO(
                career.getId(),
                career.getCareerName(),
                career.getDescription(),
                career.getRequiredSkills(),
                career.getSalaryRange(),
                career.getJobOutlook(),
                career.getRelatedUniversities(),
                career.getActive(),
                career.getCreatedAt(),
                career.getUpdatedAt(),
                null
        );
    }
}
