package com.projeto1cc.grevia.care.service;

import com.projeto1cc.grevia.care.model.CarePlan;
import com.projeto1cc.grevia.care.dto.CarePlanRequestDTO;
import com.projeto1cc.grevia.care.dto.CarePlanResponseDTO;
import com.projeto1cc.grevia.care.enums.FrequencyType;
import com.projeto1cc.grevia.care.mapper.CarePlanMapper;
import com.projeto1cc.grevia.care.repository.CarePlanRepository;
import com.projeto1cc.grevia.plant.model.Plant;
import com.projeto1cc.grevia.plant.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarePlanService {

    private final CarePlanRepository carePlanRepository;
    private final PlantRepository plantRepository;
    private final CarePlanMapper carePlanMapper;

    @Transactional
    public CarePlanResponseDTO createCarePlan(Long plantId, CarePlanRequestDTO requestDTO, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You do not have permission to manage this plant's care plans");
        }

        CarePlan carePlan = carePlanMapper.toEntity(requestDTO);
        carePlan.setPlant(plant);
        
        LocalDate baseDate = requestDTO.getStartDate() != null ? requestDTO.getStartDate() : LocalDate.now();
        carePlan.setNextCareDate(calculateNextDate(baseDate, requestDTO.getFrequencyType()));

        CarePlan savedPlan = carePlanRepository.save(carePlan);
        return carePlanMapper.toResponseDTO(savedPlan);
    }

    @Transactional(readOnly = true)
    public List<CarePlanResponseDTO> getCarePlansByPlantId(Long plantId, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You do not have permission to view this plant's care plans");
        }

        return carePlanRepository.findByPlantId(plantId).stream()
                .map(carePlanMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CarePlanResponseDTO updateCarePlan(Long plantId, Long careId, CarePlanRequestDTO requestDTO, String userEmail) {
        CarePlan carePlan = carePlanRepository.findById(careId)
                .orElseThrow(() -> new RuntimeException("Care Plan not found"));

        if (!carePlan.getPlant().getId().equals(plantId)) {
            throw new RuntimeException("Care Plan does not belong to the specified plant");
        }

        if (!carePlan.getPlant().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You do not have permission to update this care plan");
        }

        carePlanMapper.updateEntityFromDto(requestDTO, carePlan);
        
        LocalDate baseDate = requestDTO.getStartDate() != null ? requestDTO.getStartDate() : LocalDate.now();
        carePlan.setNextCareDate(calculateNextDate(baseDate, requestDTO.getFrequencyType()));

        CarePlan updatedPlan = carePlanRepository.save(carePlan);
        return carePlanMapper.toResponseDTO(updatedPlan);
    }

    @Transactional
    public void deleteCarePlan(Long plantId, Long careId, String userEmail) {
        CarePlan carePlan = carePlanRepository.findById(careId)
                .orElseThrow(() -> new RuntimeException("Care Plan not found"));

        if (!carePlan.getPlant().getId().equals(plantId)) {
            throw new RuntimeException("Care Plan does not belong to the specified plant");
        }

        if (!carePlan.getPlant().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You do not have permission to delete this care plan");
        }

        carePlanRepository.delete(carePlan);
    }

    public LocalDate calculateNextDate(LocalDate baseDate, FrequencyType frequencyType) {
        if (baseDate == null) baseDate = LocalDate.now();
        
        return switch (frequencyType) {
            case DIARIO -> baseDate.plusDays(1);
            case SEMANAL -> baseDate.plusWeeks(1);
            case QUINZENAL -> baseDate.plusWeeks(2);
            case MENSAL -> baseDate.plusMonths(1);
            case BIMESTRAL -> baseDate.plusMonths(2);
            case SOB_DEMANDA -> baseDate.plusYears(100); // effectively "never" auto-scheduled
        };
    }
}
