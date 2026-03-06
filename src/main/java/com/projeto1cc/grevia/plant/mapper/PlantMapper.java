package com.projeto1cc.grevia.plant.mapper;

import com.projeto1cc.grevia.plant.Plant;
import com.projeto1cc.grevia.plant.dto.PlantRequestDTO;
import com.projeto1cc.grevia.plant.dto.PlantResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagePath", ignore = true)
    @Mapping(target = "recommendations", ignore = true)
    @Mapping(target = "user", ignore = true)
    Plant toEntity(PlantRequestDTO dto);

    @Mapping(source = "user.id", target = "userId")
    PlantResponseDTO toResponseDTO(Plant plant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagePath", ignore = true)
    @Mapping(target = "recommendations", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(PlantRequestDTO dto, @MappingTarget Plant plant);
}
