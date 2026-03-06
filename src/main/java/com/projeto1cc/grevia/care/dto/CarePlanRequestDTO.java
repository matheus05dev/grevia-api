package com.projeto1cc.grevia.care.dto;

import com.projeto1cc.grevia.care.enums.CareType;
import com.projeto1cc.grevia.care.enums.FrequencyType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

public record CarePlanRequestDTO(
    @NotNull(message = "O tipo de cuidado é obrigatório") CareType careType,
    @NotNull(message = "A frequência de cuidado é obrigatória") FrequencyType frequencyType,
    LocalDate startDate
) {}
