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
                .orElseThrow(() -> new RuntimeException("Planta não encontrada"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para gerenciar os planos de cuidado desta planta");
        }

        CarePlan carePlan = carePlanMapper.toEntity(requestDTO);
        carePlan.setPlant(plant);
        
        LocalDate initialDate = requestDTO.startDate() != null ? requestDTO.startDate() : LocalDate.now();
        carePlan.setNextCareDate(initialDate);

        CarePlan savedPlan = carePlanRepository.save(carePlan);
        return carePlanMapper.toResponseDTO(savedPlan);
    }

    @Transactional(readOnly = true)
    public List<CarePlanResponseDTO> getCarePlansByPlantId(Long plantId, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Planta não encontrada"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para visualizar os planos de cuidado desta planta");
        }

        return carePlanRepository.findByPlantId(plantId).stream()
                .map(carePlanMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CarePlanResponseDTO updateCarePlan(Long plantId, Long careId, CarePlanRequestDTO requestDTO, String userEmail) {
        CarePlan carePlan = carePlanRepository.findById(careId)
                .orElseThrow(() -> new RuntimeException("Plano de cuidado não encontrado"));

        if (!carePlan.getPlant().getId().equals(plantId)) {
            throw new RuntimeException("O plano de cuidado não pertence a planta especificada");
        }

        if (!carePlan.getPlant().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para atualizar esse plano de cuidado");
        }

        carePlanMapper.updateEntityFromDto(requestDTO, carePlan);
        
        if (requestDTO.startDate() != null) {
            carePlan.setNextCareDate(requestDTO.startDate());
        } else if (carePlan.getNextCareDate() == null) {
            carePlan.setNextCareDate(LocalDate.now());
        }

        CarePlan updatedPlan = carePlanRepository.save(carePlan);
        return carePlanMapper.toResponseDTO(updatedPlan);
    }

    @Transactional
    public void deleteCarePlan(Long plantId, Long careId, String userEmail) {
        CarePlan carePlan = carePlanRepository.findById(careId)
                .orElseThrow(() -> new RuntimeException("Plano de cuidado não encontrado"));

        if (!carePlan.getPlant().getId().equals(plantId)) {
            throw new RuntimeException("O plano de cuidado não pertence a planta especificada");
        }

        if (!carePlan.getPlant().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para deletar esse plano de cuidado");
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
