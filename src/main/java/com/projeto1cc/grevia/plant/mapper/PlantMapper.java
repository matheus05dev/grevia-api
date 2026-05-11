package com.projeto1cc.grevia.plant.mapper;

import com.projeto1cc.grevia.plant.model.Plant;
import com.projeto1cc.grevia.plant.dto.PlantRequestDTO;
import com.projeto1cc.grevia.plant.dto.PlantResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recommendations", ignore = true)
    @Mapping(target = "user", ignore = true)
    Plant toEntity(PlantRequestDTO dto);

    @Mapping(source = "user.name", target = "ownerName")
    @Mapping(target = "utility", expression = "java(plant.getSpecies() != null ? plant.getSpecies().getUtility() : null)")
    PlantResponseDTO toResponseDTO(Plant plant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recommendations", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "harvestedAt", ignore = true)
    @Mapping(target = "archivedAt", ignore = true)
    @Mapping(target = "historyNotes", ignore = true)
    @Mapping(target = "carePlans", ignore = true)
    void updateEntityFromDto(PlantRequestDTO dto, @MappingTarget Plant plant);

    default com.projeto1cc.grevia.plant.dto.HistoryResponseDTO toHistoryResponseDTO(Plant plant) {
        if (plant == null) {
            return null;
        }

        Integer waterEveryDays = null;
        if (plant.getCarePlans() != null) {
            waterEveryDays = plant.getCarePlans().stream()
                    .filter(cp -> com.projeto1cc.grevia.care.enums.CareType.REGA.equals(cp.getCareType()))
                    .findFirst()
                    .map(cp -> {
                        String freq = cp.getFrequencyType() != null ? cp.getFrequencyType().name() : "";
                        if ("DIARIO".equals(freq)) return 1;
                        if ("TRES_VEZES_SEMANA".equals(freq)) return 2;
                        if ("DUAS_VEZES_SEMANA".equals(freq)) return 3;
                        if ("SEMANAL".equals(freq)) return 7;
                        if ("QUINZENAL".equals(freq)) return 15;
                        if ("MENSAL".equals(freq)) return 30;
                        if ("BIMESTRAL".equals(freq)) return 60;
                        return 3;
                    })
                    .orElse(null);
        }

        return new com.projeto1cc.grevia.plant.dto.HistoryResponseDTO(
                plant.getId(),
                plant.getName(),
                plant.getSpecies(),
                plant.getCustomSpeciesName(),
                plant.getSpecies() != null ? plant.getSpecies().getUtility() : null,
                plant.getStatus(),
                plant.getRegisteredAt(),
                plant.getHarvestedAt(),
                plant.getArchivedAt(),
                plant.getHistoryNotes(),
                plant.getSpaceType(),
                plant.getSpaceSize(),
                plant.getSunHours(),
                waterEveryDays
        );
    }
}
