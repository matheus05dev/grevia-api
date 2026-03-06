package com.projeto1cc.grevia.plant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record SpeciesSuggestionRequestDTO(
    @NotBlank(message = "O nome sugerido da espécie é obrigatório") String suggestedName,
    String description
) {}
