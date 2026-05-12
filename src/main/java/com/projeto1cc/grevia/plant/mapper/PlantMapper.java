package com.projeto1cc.grevia.plant.mapper;

import com.projeto1cc.grevia.care.enums.CareType;
import com.projeto1cc.grevia.plant.model.Plant;
import com.projeto1cc.grevia.plant.dto.HistoryResponseDTO;
import com.projeto1cc.grevia.plant.dto.PlantRequestDTO;
import com.projeto1cc.grevia.plant.dto.PlantResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring")
public interface PlantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recommendations", ignore = true)
    @Mapping(target = "user", ignore = true)
    Plant toEntity(PlantRequestDTO dto);

    default PlantResponseDTO toResponseDTO(Plant plant) {
        if (plant == null) return null;

        String ownerName = (plant.getUser() != null) ? plant.getUser().getName() : null;
        var utility = (plant.getSpecies() != null) ? plant.getSpecies().getUtility() : null;
        String utilityDisplayName = (utility != null) ? utility.getDisplayName() : null;
        String soilTypeDisplayName = (plant.getSoilType() != null) ? plant.getSoilType().getDisplayName() : null;

        // calcula porcentagem de progresso e dias restantes
        Double progressPercentage = null;
        Integer daysRemaining = null;
        if (plant.getSpecies() != null && plant.getRegisteredAt() != null) {
            int maturityDays = plant.getSpecies().getMaturityDays();
            long daysSinceCreation = ChronoUnit.DAYS.between(plant.getRegisteredAt(), LocalDate.now());
            
            // "Care boost": se foi cuidada hoje ou no passado, adiciona 1 dia à idade efetiva
            // Isso garante que plantas novas mostrem algum progresso (1%+) no primeiro dia se cuidadas.
            boolean hasBeenCared = plant.getCarePlans() != null && plant.getCarePlans().stream()
                    .anyMatch(cp -> cp.getLastCareDate() != null);
            
            long effectiveDays = hasBeenCared ? daysSinceCreation + 1 : daysSinceCreation;
            
            double rawPct = (effectiveDays * 100.0) / Math.max(1, maturityDays);
            progressPercentage = Math.min(100.0, Math.round(rawPct * 10.0) / 10.0);
            daysRemaining = Math.max(0, maturityDays - (int) effectiveDays);
        }

        return new PlantResponseDTO(
            plant.getId(),
            plant.getName(),
            plant.getSpecies(),
            plant.getCustomSpeciesName(),
            plant.getRecommendations(),
            plant.getSoilType(),
            ownerName,
            utility,
            utilityDisplayName,
            soilTypeDisplayName,
            progressPercentage,
            daysRemaining,
            plant.getStatus() != null ? plant.getStatus().name() : null
        );
    }

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

    default HistoryResponseDTO toHistoryResponseDTO(Plant plant) {
        if (plant == null) {
            return null;
        }

        Integer waterEveryDays = null;
        if (plant.getCarePlans() != null) {
            waterEveryDays = plant.getCarePlans().stream()
                    .filter(cp -> CareType.REGA.equals(cp.getCareType()))
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

        return new HistoryResponseDTO(
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
