package com.projeto1cc.grevia.care.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CareRecordRequestDTO {
    private String notes;
    private LocalDate careDate; // Optional, defaults to today
}
