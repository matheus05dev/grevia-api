package com.projeto1cc.grevia.care.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CareRecordResponseDTO {
    private Long id;
    private String notes;
    private LocalDate careDate;
    private Long carePlanId;
}
