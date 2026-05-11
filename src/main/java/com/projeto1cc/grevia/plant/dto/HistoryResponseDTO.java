package com.projeto1cc.grevia.plant.dto;

import com.projeto1cc.grevia.plant.enums.PlantStatus;
import com.projeto1cc.grevia.plant.enums.PlantUtility;
import com.projeto1cc.grevia.plant.enums.Species;

import java.time.LocalDate;

public record HistoryResponseDTO(
    Long id,
    String name,
    Species species,
    String customSpeciesName,
    PlantUtility utility,
    PlantStatus status,
    LocalDate registeredAt,
    LocalDate harvestedAt,
    LocalDate archivedAt,
    String historyNotes,
    String spaceType,
    String spaceSize,
    Integer sunHours,
    Integer waterEveryDays // Calculado a partir dos planos de cuidados durante o mapeamento
) {}
