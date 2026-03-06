package com.projeto1cc.grevia.care.dto;

import lombok.Data;

import java.time.LocalDate;

public record CareRecordResponseDTO(
    Long id,
    String notes,
    LocalDate careDate,
    Long carePlanId
) {}
