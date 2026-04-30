package com.projeto1cc.grevia.plant.controller;

import com.projeto1cc.grevia.plant.dto.PlantRequestDTO;
import com.projeto1cc.grevia.plant.dto.PlantResponseDTO;
import com.projeto1cc.grevia.plant.service.PlantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
@RequiredArgsConstructor
public class PlantRestController {

    private final PlantService plantService;
    @PostMapping
    public ResponseEntity<PlantResponseDTO> createPlant(@Valid @RequestBody PlantRequestDTO requestDTO) {
        return ResponseEntity.ok(plantService.createPlant(requestDTO, getAuthenticatedUserEmail()));
    }

    @GetMapping
    public ResponseEntity<List<PlantResponseDTO>> getUserPlants() {
        return ResponseEntity.ok(plantService.getUserPlants(getAuthenticatedUserEmail()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantResponseDTO> getPlantById(@PathVariable Long id) {
        return ResponseEntity.ok(plantService.getPlantById(id, getAuthenticatedUserEmail()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantResponseDTO> updatePlant(@PathVariable Long id, @Valid @RequestBody PlantRequestDTO requestDTO) {
        return ResponseEntity.ok(plantService.updatePlant(id, requestDTO, getAuthenticatedUserEmail()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id, getAuthenticatedUserEmail());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PlantResponseDTO>> getFeed() {
        return ResponseEntity.ok(plantService.getFeed());
    }



    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
