package com.projeto1cc.grevia.care.service;

import com.projeto1cc.grevia.care.dto.CarePlanRequestDTO;
import com.projeto1cc.grevia.care.enums.CareType;
import com.projeto1cc.grevia.care.enums.FrequencyType;
import com.projeto1cc.grevia.plant.enums.Species;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpeciesCareService {

    public List<CarePlanRequestDTO> getDefaultCaresForSpecies(Species species) {
        List<CarePlanRequestDTO> cares = new ArrayList<>();
        if (species == null || species == Species.OUTRA) {
            return cares;
        }

        switch (species) {
            case CACTO:
            case SUCULENTA:
            case ZAMIOCULCA:
                cares.add(createCare(CareType.REGA, FrequencyType.QUINZENAL, 0));
                cares.add(createCare(CareType.ADUBACAO, FrequencyType.BIMESTRAL, 15));
                break;
            case SAMAMBAIA:
            case JIBOIA:
            case LIRIO_DA_PAZ:
                cares.add(createCare(CareType.REGA, FrequencyType.SEMANAL, 0));
                cares.add(createCare(CareType.PODA, FrequencyType.MENSAL, 10));
                cares.add(createCare(CareType.ADUBACAO, FrequencyType.MENSAL, 20));
                break;
            case ESPADA_DE_SAO_JORGE:
            case COSTELA_DE_ADAO:
            case ORQUIDEA:
                cares.add(createCare(CareType.REGA, FrequencyType.SEMANAL, 0));
                cares.add(createCare(CareType.ADUBACAO, FrequencyType.BIMESTRAL, 15));
                cares.add(createCare(CareType.CONTROLE_PRAGAS, FrequencyType.SEMANAL, 30));
                break;
            case ROSA:
                cares.add(createCare(CareType.REGA, FrequencyType.SEMANAL, 0));
                cares.add(createCare(CareType.PODA, FrequencyType.MENSAL, 10));
                cares.add(createCare(CareType.ADUBACAO, FrequencyType.MENSAL, 14));
                cares.add(createCare(CareType.CONTROLE_PRAGAS, FrequencyType.QUINZENAL, 5));
                break;
            case TOMATE:
            case PIMENTA:
            case MORANGO:
                cares.add(createCare(CareType.REGA, FrequencyType.DIARIO, 0)); // Common for fruiting plants in full sun
                cares.add(createCare(CareType.ADUBACAO, FrequencyType.MENSAL, 15));
                break;
            case ALFACE:
            case CEBOLINHA:
            case SALSINHA:
            case MANJERICAO:
            case HORTELA:
                cares.add(createCare(CareType.REGA, FrequencyType.DIARIO, 0));
                cares.add(createCare(CareType.PODA, FrequencyType.SOB_DEMANDA, 20)); // Harvesting
                break;
        }

        return cares;
    }

    private CarePlanRequestDTO createCare(CareType careType, FrequencyType frequencyType, int initialOffsetDays) {
        return new CarePlanRequestDTO(careType, frequencyType, LocalDate.now().plusDays(initialOffsetDays));
    }
}
