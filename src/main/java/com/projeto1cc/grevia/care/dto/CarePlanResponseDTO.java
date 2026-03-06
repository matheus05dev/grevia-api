package com.projeto1cc.grevia.care.dto;

import com.projeto1cc.grevia.care.enums.CareType;
import com.projeto1cc.grevia.care.enums.FrequencyType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CarePlanResponseDTO {

    private Long id;
    private CareType careType;
    private FrequencyType frequencyType;
    private LocalDate nextCareDate;
    private Long plantId;
}
