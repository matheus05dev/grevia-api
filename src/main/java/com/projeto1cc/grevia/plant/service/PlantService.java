package com.projeto1cc.grevia.plant.service;

import com.projeto1cc.grevia.plant.model.Plant;
import com.projeto1cc.grevia.plant.dto.PlantRequestDTO;
import com.projeto1cc.grevia.plant.dto.PlantResponseDTO;
import com.projeto1cc.grevia.plant.mapper.PlantMapper;
import com.projeto1cc.grevia.plant.repository.PlantRepository;
import com.projeto1cc.grevia.core.storage.FileStorageService;
import com.projeto1cc.grevia.care.service.CarePlanService;
import com.projeto1cc.grevia.care.service.SpeciesCareService;
import com.projeto1cc.grevia.care.dto.CarePlanRequestDTO;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    // Removing the commented line
    private final FileStorageService fileStorageService;

    @Transactional
    public PlantResponseDTO createPlant(PlantRequestDTO requestDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Plant plant = plantMapper.toEntity(requestDTO);
        plant.setUser(user);
        plant.setRecommendations(recommendationService.generateRecommendation(plant.getSoilType(), plant.getSpecies()));

        Plant savedPlant = plantRepository.save(plant);

        // Auto-generate care plans based on species
        List<CarePlanRequestDTO> defaultCares = speciesCareService.getDefaultCaresForSpecies(savedPlant.getSpecies());
        for (CarePlanRequestDTO careRequest : defaultCares) {
            carePlanService.createCarePlan(savedPlant.getId(), careRequest, userEmail);
        }

        return plantMapper.toResponseDTO(savedPlant);
    }

    @Transactional(readOnly = true)
    public List<PlantResponseDTO> getUserPlants(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return plantRepository.findByUserId(user.getId()).stream()
                .map(plantMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlantResponseDTO getPlantById(Long plantId, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Planta não encontrada"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para acessar esta planta");
        }

        return plantMapper.toResponseDTO(plant);
    }

    @Transactional
    public PlantResponseDTO updatePlant(Long plantId, PlantRequestDTO requestDTO, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Planta não encontrada"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para atualizar esta planta");
        }

        plantMapper.updateEntityFromDto(requestDTO, plant);
        
        // Update recommendations if soil type changed
        plant.setRecommendations(recommendationService.generateRecommendation(plant.getSoilType(), plant.getSpecies()));

        Plant updatedPlant = plantRepository.save(plant);
        return plantMapper.toResponseDTO(updatedPlant);
    }

    @Transactional
    public void deletePlant(Long plantId, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Planta não encontrada"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para deletar esta planta");
        }

        plantRepository.delete(plant);
    }

    @Transactional(readOnly = true)
    public List<PlantResponseDTO> getFeed() {
        return plantRepository.findAll().stream()
                .map(plantMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlantResponseDTO uploadPlantImage(Long plantId, MultipartFile file, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Planta não encontrada"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para atualizar esta planta");
        }

        String fileName = fileStorageService.storeFile(file);
        plant.setImagePath(fileName);
        Plant updatedPlant = plantRepository.save(plant);
        
        return plantMapper.toResponseDTO(updatedPlant);
    }
}
