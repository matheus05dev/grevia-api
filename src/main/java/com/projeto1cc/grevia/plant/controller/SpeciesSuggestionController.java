package com.projeto1cc.grevia.plant.controller;

import com.projeto1cc.grevia.plant.dto.SpeciesSuggestionRequestDTO;
import com.projeto1cc.grevia.plant.dto.SpeciesSuggestionResponseDTO;
import com.projeto1cc.grevia.plant.service.SpeciesSuggestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/species/suggestions")
@RequiredArgsConstructor
public class SpeciesSuggestionController {

    private final SpeciesSuggestionService service;

    @PostMapping
    public ResponseEntity<SpeciesSuggestionResponseDTO> submitSuggestion(@Valid @RequestBody SpeciesSuggestionRequestDTO requestDTO) {
        return ResponseEntity.ok(service.submitSuggestion(requestDTO, getAuthenticatedUserEmail()));
    }

    // Usually restricted to Admins, but letting it open to see community suggestions
    @GetMapping
    public ResponseEntity<List<SpeciesSuggestionResponseDTO>> getAllSuggestions() {
        return ResponseEntity.ok(service.getAllSuggestions());
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
