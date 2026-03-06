package com.projeto1cc.grevia.care.dto;

import com.projeto1cc.grevia.care.enums.CareType;
import com.projeto1cc.grevia.care.enums.FrequencyType;
import lombok.Data;

import java.time.LocalDate;

public record CarePlanResponseDTO(
    Long id,
    CareType careType,
    FrequencyType frequencyType,
    LocalDate nextCareDate,
    Long plantId
) {}
