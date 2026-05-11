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

import com.projeto1cc.grevia.plant.dto.SpeciesDTO;
import com.projeto1cc.grevia.plant.enums.Species;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/history")
    public ResponseEntity<org.springframework.data.domain.Page<com.projeto1cc.grevia.plant.dto.HistoryResponseDTO>> getPlantHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return ResponseEntity.ok(plantService.getPlantHistory(getAuthenticatedUserEmail(), pageable));
    }

    @PatchMapping("/{id}/harvest")
    public ResponseEntity<Void> harvestPlant(@PathVariable Long id) {
        plantService.harvestPlant(id, getAuthenticatedUserEmail());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archivePlant(@PathVariable Long id, @RequestBody(required = false) String notes) {
        plantService.archivePlant(id, notes, getAuthenticatedUserEmail());
        return ResponseEntity.noContent().build();
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

    @GetMapping("/species")
    public ResponseEntity<List<SpeciesDTO>> getAllSpecies() {
        return ResponseEntity.ok(Arrays.stream(Species.values())
                .map(s -> new SpeciesDTO(
                        s.name(),
                        formatSpeciesName(s.name()),
                        s.getUtility().name()
                ))
                .collect(Collectors.toList()));
    }

    private String formatSpeciesName(String name) {
        if (name == null || name.isEmpty()) return "";
        String lowerCase = name.replace("_", " ").toLowerCase();
        String[] words = lowerCase.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                if (word.equals("de") || word.equals("da") || word.equals("do")) {
                    sb.append(word).append(" ");
                } else {
                    sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
                }
            }
        }
        return sb.toString().trim();
    }



    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
