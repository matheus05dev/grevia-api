package com.projeto1cc.grevia.plant.dto;

import com.projeto1cc.grevia.plant.enums.SoilType;
import lombok.Data;

@Data
public class PlantResponseDTO {
    private Long id;
    private String name;
    private String species;
    private String recommendations;
    private SoilType soilType;
    private String imagePath;
    private Long userId;
}
