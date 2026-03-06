package com.projeto1cc.grevia.care.controller;

import com.projeto1cc.grevia.care.dto.CarePlanRequestDTO;
import com.projeto1cc.grevia.care.dto.CarePlanResponseDTO;
import com.projeto1cc.grevia.care.service.CarePlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants/{plantId}/cares")
@RequiredArgsConstructor
public class CarePlanController {

    private final CarePlanService carePlanService;

    @PostMapping
    public ResponseEntity<CarePlanResponseDTO> createCarePlan(
            @PathVariable Long plantId,
            @Valid @RequestBody CarePlanRequestDTO requestDTO) {
        return ResponseEntity.ok(carePlanService.createCarePlan(plantId, requestDTO, getAuthenticatedUserEmail()));
    }

    @GetMapping
    public ResponseEntity<List<CarePlanResponseDTO>> getCarePlans(@PathVariable Long plantId) {
        return ResponseEntity.ok(carePlanService.getCarePlansByPlantId(plantId, getAuthenticatedUserEmail()));
    }

    @PutMapping("/{careId}")
    public ResponseEntity<CarePlanResponseDTO> updateCarePlan(
            @PathVariable Long plantId,
            @PathVariable Long careId,
            @Valid @RequestBody CarePlanRequestDTO requestDTO) {
        return ResponseEntity.ok(carePlanService.updateCarePlan(plantId, careId, requestDTO, getAuthenticatedUserEmail()));
    }

    @DeleteMapping("/{careId}")
    public ResponseEntity<Void> deleteCarePlan(
            @PathVariable Long plantId,
            @PathVariable Long careId) {
        carePlanService.deleteCarePlan(plantId, careId, getAuthenticatedUserEmail());
        return ResponseEntity.noContent().build();
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
