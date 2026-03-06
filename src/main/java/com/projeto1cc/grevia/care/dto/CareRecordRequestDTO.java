package com.projeto1cc.grevia.care.dto;

import lombok.Data;

import java.time.LocalDate;

public record CareRecordRequestDTO(
    String notes,
    LocalDate careDate
) {}
