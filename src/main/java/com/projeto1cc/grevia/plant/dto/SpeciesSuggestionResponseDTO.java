package com.projeto1cc.grevia.plant.dto;

import lombok.Data;
import java.time.LocalDateTime;

public record SpeciesSuggestionResponseDTO(
    Long id,
    String suggestedName,
    String description,
    Long submittedById,
    LocalDateTime submittedAt
) {}
