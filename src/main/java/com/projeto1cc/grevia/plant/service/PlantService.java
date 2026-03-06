package com.projeto1cc.grevia.plant.service;

import com.projeto1cc.grevia.plant.Plant;
import com.projeto1cc.grevia.plant.dto.PlantRequestDTO;
import com.projeto1cc.grevia.plant.dto.PlantResponseDTO;
import com.projeto1cc.grevia.plant.mapper.PlantMapper;
import com.projeto1cc.grevia.plant.repository.PlantRepository;
import com.projeto1cc.grevia.core.storage.FileStorageService;
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
    // Removing the commented line
    private final FileStorageService fileStorageService;

    @Transactional
    public PlantResponseDTO createPlant(PlantRequestDTO requestDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Plant plant = plantMapper.toEntity(requestDTO);
        plant.setUser(user);
        plant.setRecommendations(recommendationService.generateRecommendation(plant.getSoilType()));

        Plant savedPlant = plantRepository.save(plant);
        return plantMapper.toResponseDTO(savedPlant);
    }

    @Transactional(readOnly = true)
    public List<PlantResponseDTO> getUserPlants(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return plantRepository.findByUserId(user.getId()).stream()
                .map(plantMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlantResponseDTO getPlantById(Long plantId, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You do not have permission to access this plant");
        }

        return plantMapper.toResponseDTO(plant);
    }

    @Transactional
    public PlantResponseDTO updatePlant(Long plantId, PlantRequestDTO requestDTO, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You do not have permission to update this plant");
        }

        plantMapper.updateEntityFromDto(requestDTO, plant);
        
        // Update recommendations if soil type changed
        plant.setRecommendations(recommendationService.generateRecommendation(plant.getSoilType()));

        Plant updatedPlant = plantRepository.save(plant);
        return plantMapper.toResponseDTO(updatedPlant);
    }

    @Transactional
    public void deletePlant(Long plantId, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You do not have permission to delete this plant");
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
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You do not have permission to update this plant");
        }

        String fileName = fileStorageService.storeFile(file);
        plant.setImagePath(fileName);
        Plant updatedPlant = plantRepository.save(plant);
        
        return plantMapper.toResponseDTO(updatedPlant);
    }
}
