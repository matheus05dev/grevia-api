package com.projeto1cc.grevia.plant.service;

import com.projeto1cc.grevia.plant.model.Plant;
import com.projeto1cc.grevia.plant.dto.PlantRequestDTO;
import com.projeto1cc.grevia.plant.dto.PlantResponseDTO;
import com.projeto1cc.grevia.plant.dto.HistoryResponseDTO;
import com.projeto1cc.grevia.plant.enums.PlantStatus;
import com.projeto1cc.grevia.plant.mapper.PlantMapper;
import com.projeto1cc.grevia.plant.repository.PlantRepository;
import com.projeto1cc.grevia.care.model.CarePlan;
import com.projeto1cc.grevia.care.repository.CarePlanRepository;
import com.projeto1cc.grevia.care.service.CarePlanService;
import com.projeto1cc.grevia.care.service.SpeciesCareService;
import com.projeto1cc.grevia.care.dto.CarePlanRequestDTO;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final PlantMapper plantMapper;
    private final PlantRecommendationService recommendationService;
    private final SpeciesCareService speciesCareService;
    private final CarePlanService carePlanService;
    private final CarePlanRepository carePlanRepository;

    @Transactional
    public PlantResponseDTO createPlant(PlantRequestDTO requestDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Plant plant = plantMapper.toEntity(requestDTO);
        plant.setUser(user);
        plant.setRecommendations(recommendationService.generateRecommendation(plant.getSoilType(), plant.getSpecies()));

        Plant savedPlant = plantRepository.save(plant);

        // Auto-generate care plans based on species (single batch INSERT)
        List<CarePlanRequestDTO> defaultCares = speciesCareService.getDefaultCaresForSpecies(savedPlant.getSpecies());
        List<CarePlan> carePlans = defaultCares.stream().map(careRequest -> {
            CarePlan carePlan = new CarePlan();
            carePlan.setCareType(careRequest.careType());
            carePlan.setFrequencyType(careRequest.frequencyType());
            carePlan.setNextCareDate(careRequest.startDate() != null ? careRequest.startDate() : java.time.LocalDate.now());
            carePlan.setPlant(savedPlant);
            return carePlan;
        }).collect(Collectors.toList());
        carePlanRepository.saveAll(carePlans);

        return plantMapper.toResponseDTO(savedPlant);
    }

    @Transactional(readOnly = true)
    public List<PlantResponseDTO> getUserPlants(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return plantRepository.findByUserIdAndStatus(user.getId(), PlantStatus.ACTIVE).stream()
                .map(plantMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<HistoryResponseDTO> getPlantHistory(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return plantRepository.findByUserId(user.getId(), pageable)
                .map(plantMapper::toHistoryResponseDTO);
    }

    @Transactional
    public void harvestPlant(Long plantId, String userEmail) {
        Plant plant = getPlantAndValidateOwnership(plantId, userEmail);
        plant.setStatus(PlantStatus.HARVESTED);
        plant.setHarvestedAt(LocalDate.now());
        plantRepository.save(plant);
    }

    @Transactional
    public void archivePlant(Long plantId, String notes, String userEmail) {
        Plant plant = getPlantAndValidateOwnership(plantId, userEmail);
        plant.setStatus(PlantStatus.ARCHIVED);
        plant.setArchivedAt(LocalDate.now());
        plant.setHistoryNotes(notes);
        plantRepository.save(plant);
    }

    private Plant getPlantAndValidateOwnership(Long plantId, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Planta não encontrada"));
        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para acessar esta planta");
        }
        return plant;
    }

    @Transactional(readOnly = true)
    public PlantResponseDTO getPlantById(Long plantId, String userEmail) {
        return plantMapper.toResponseDTO(getPlantAndValidateOwnership(plantId, userEmail));
    }

    @Transactional
    public PlantResponseDTO updatePlant(Long plantId, PlantRequestDTO requestDTO, String userEmail) {
        Plant plant = getPlantAndValidateOwnership(plantId, userEmail);

        plantMapper.updateEntityFromDto(requestDTO, plant);
        

        plant.setRecommendations(recommendationService.generateRecommendation(plant.getSoilType(), plant.getSpecies()));

        Plant updatedPlant = plantRepository.save(plant);
        return plantMapper.toResponseDTO(updatedPlant);
    }

    @Transactional
    public void deletePlant(Long plantId, String userEmail) {
        Plant plant = getPlantAndValidateOwnership(plantId, userEmail);
        plantRepository.delete(plant);
    }

    @Transactional(readOnly = true)
    public List<PlantResponseDTO> getFeed() {
        return plantRepository.findAll().stream()
                .map(plantMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
