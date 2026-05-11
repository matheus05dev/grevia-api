package com.projeto1cc.grevia.core.feedback.controller;

import com.projeto1cc.grevia.core.feedback.dto.AppFeedbackRequestDTO;
import com.projeto1cc.grevia.core.feedback.dto.AppFeedbackResponseDTO;
import com.projeto1cc.grevia.core.feedback.service.AppFeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class AppFeedbackRestController {

    private final AppFeedbackService service;

    @PostMapping
    public ResponseEntity<AppFeedbackResponseDTO> submitFeedback(@Valid @RequestBody AppFeedbackRequestDTO requestDTO) {
        return ResponseEntity.ok(service.submitFeedback(requestDTO, getAuthenticatedUserEmail()));
    }

    @GetMapping
    public ResponseEntity<List<AppFeedbackResponseDTO>> getAllFeedbacks() {
        return ResponseEntity.ok(service.getAllFeedbacks());
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
