package com.projeto1cc.grevia.care.controller;

import com.projeto1cc.grevia.care.dto.CareRecordRequestDTO;
import com.projeto1cc.grevia.care.dto.CareRecordResponseDTO;
import com.projeto1cc.grevia.care.service.CareRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cares/{carePlanId}/records")
@RequiredArgsConstructor
public class CareRecordRestController {

    private final CareRecordService careRecordService;

    @PostMapping
    public ResponseEntity<CareRecordResponseDTO> addCareRecord(
            @PathVariable Long carePlanId,
            @Valid @RequestBody CareRecordRequestDTO requestDTO) {
        return ResponseEntity.ok(careRecordService.createCareRecord(carePlanId, requestDTO, getAuthenticatedUserEmail()));
    }

    @GetMapping
    public ResponseEntity<List<CareRecordResponseDTO>> getCareRecords(@PathVariable Long carePlanId) {
        return ResponseEntity.ok(careRecordService.getCareRecordsByPlanId(carePlanId, getAuthenticatedUserEmail()));
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
