package com.projeto1cc.grevia.care.dto;

import com.projeto1cc.grevia.care.enums.CareType;
import com.projeto1cc.grevia.care.enums.FrequencyType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CarePlanRequestDTO {

    @NotNull(message = "Care type is required")
    private CareType careType;

    @NotNull(message = "Frequency is required")
    private FrequencyType frequencyType;

    private LocalDate startDate; // Optional, defaults to today
}
