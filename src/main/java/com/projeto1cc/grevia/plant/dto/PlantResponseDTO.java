package com.projeto1cc.grevia.plant.dto;

import com.projeto1cc.grevia.plant.enums.SoilType;
import com.projeto1cc.grevia.plant.enums.Species;
import lombok.Data;

public record PlantResponseDTO(
    Long id,
    String name,
    Species species,
    String customSpeciesName,
    String recommendations,
    SoilType soilType,
    String imagePath,
    String ownerName
) {}
