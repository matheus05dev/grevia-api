package com.projeto1cc.grevia.plant.dto;

import com.projeto1cc.grevia.plant.enums.SoilType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlantRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String species;

    @NotNull(message = "Soil type is required")
    private SoilType soilType;
}
