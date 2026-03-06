package com.projeto1cc.grevia.plant.dto;

import com.projeto1cc.grevia.plant.enums.SoilType;
import com.projeto1cc.grevia.plant.enums.Species;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public record PlantRequestDTO(
    @NotBlank(message = "O nome da planta é obrigatório") String name,
    @NotNull(message = "O tipo de espécie é obrigatório") Species species,
    String customSpeciesName,
    @NotNull(message = "O tipo de solo é obrigatório") SoilType soilType
) {}
