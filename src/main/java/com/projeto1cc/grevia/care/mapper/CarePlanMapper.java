package com.projeto1cc.grevia.care.mapper;

import com.projeto1cc.grevia.care.model.CarePlan;
import com.projeto1cc.grevia.care.dto.CarePlanRequestDTO;
import com.projeto1cc.grevia.care.dto.CarePlanResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CarePlanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nextCareDate", ignore = true)
    @Mapping(target = "lastCareDate", ignore = true)
    @Mapping(target = "plant", ignore = true)
    CarePlan toEntity(CarePlanRequestDTO dto);

    default CarePlanResponseDTO toResponseDTO(CarePlan carePlan) {
        return toResponseDTO(carePlan, null, null);
    }

    default CarePlanResponseDTO toResponseDTO(CarePlan carePlan, Boolean levelUp, String newLevel) {
        if (carePlan == null) return null;
        return new CarePlanResponseDTO(
            carePlan.getId(),
            carePlan.getCareType(),
            carePlan.getFrequencyType(),
            carePlan.getNextCareDate(),
            carePlan.getLastCareDate(),
            carePlan.getPlant() != null ? carePlan.getPlant().getId() : null,
            levelUp,
            newLevel
        );
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nextCareDate", ignore = true)
    @Mapping(target = "lastCareDate", ignore = true)
    @Mapping(target = "plant", ignore = true)
    void updateEntityFromDto(CarePlanRequestDTO dto, @MappingTarget CarePlan carePlan);
}
