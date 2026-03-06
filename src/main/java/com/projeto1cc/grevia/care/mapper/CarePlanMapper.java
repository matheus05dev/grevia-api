package com.projeto1cc.grevia.care.mapper;

import com.projeto1cc.grevia.care.CarePlan;
import com.projeto1cc.grevia.care.dto.CarePlanRequestDTO;
import com.projeto1cc.grevia.care.dto.CarePlanResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CarePlanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nextCareDate", ignore = true)
    @Mapping(target = "plant", ignore = true)
    CarePlan toEntity(CarePlanRequestDTO dto);

    @Mapping(source = "plant.id", target = "plantId")
    CarePlanResponseDTO toResponseDTO(CarePlan carePlan);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nextCareDate", ignore = true)
    @Mapping(target = "plant", ignore = true)
    void updateEntityFromDto(CarePlanRequestDTO dto, @MappingTarget CarePlan carePlan);
}
