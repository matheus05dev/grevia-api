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
            case ESPADA_DE_SAO_JORGE:
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
            case COSTELA_DE_ADAO:
            case ORQUIDEA:
                cares.add(createCare(CareType.REGA, FrequencyType.SEMANAL, 0));
                cares.add(createCare(CareType.ADUBACAO, FrequencyType.BIMESTRAL, 15));
                cares.add(createCare(CareType.CONTROLE_PRAGAS, FrequencyType.QUINZENAL, 30));
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
            case RUCULA:
            case ESPINAFRE:
            case TOMATE_CEREJA:
            case PIMENTAO:
            case PEPINO:
            case ABOBRINHA:
            case BERINJELA:
            case RABANETE:
            case ALFACE:
            case AGRIAO:
                cares.add(createCare(CareType.REGA, FrequencyType.DIARIO, 0));
                cares.add(createCare(CareType.ADUBACAO, FrequencyType.MENSAL, 15));
                cares.add(createCare(CareType.PODA, FrequencyType.SOB_DEMANDA, 20)); // Harvesting
                break;
            case COUVE:
            case CENOURA:
            case BETERRABA:
            case ALHO:
            case CEBOLA:
            case ERVILHA:
            case FEIJAO:
            case FEIJAO_VAGEM:
            case MILHO_ANAO:
            case BATATA:
            case BATATA_DOCE:
            case ALMEIRAO:
            case ACELGA:
            case MOSTARDA:
            case ENDIVIA:
            case FUNCHO:
            case CAPIM_LIMAO:
            case CHICORIA:
            case NABO:
            case QUIABO:
            case AMENDOIM:
            case GENGIBRE:
            case CURCUMA:
            case STEVIA:
            case MELISSA:
            case MARACUJA:
            case MELANCIA_MINI:
            case CEBOLINHA:
            case SALSINHA:
            case MANJERICAO:
            case HORTELA:
            case COENTRO:
                cares.add(createCare(CareType.REGA, FrequencyType.TRES_VEZES_SEMANA, 0));
                cares.add(createCare(CareType.ADUBACAO, FrequencyType.MENSAL, 15));
                cares.add(createCare(CareType.PODA, FrequencyType.SOB_DEMANDA, 30));
                break;
            case TOMILHO:
            case ALECRIM:
            case OREGANO:
            case LOURO:
            case SALVIA:
            case LAVANDA:
                cares.add(createCare(CareType.REGA, FrequencyType.DUAS_VEZES_SEMANA, 0));
                cares.add(createCare(CareType.ADUBACAO, FrequencyType.BIMESTRAL, 15));
                cares.add(createCare(CareType.PODA, FrequencyType.SOB_DEMANDA, 20)); // Harvesting
                break;
            case OUTRA:
                break;
        }

        return cares;
    }

    private CarePlanRequestDTO createCare(CareType careType, FrequencyType frequencyType, int initialOffsetDays) {
        return new CarePlanRequestDTO(careType, frequencyType, LocalDate.now().plusDays(initialOffsetDays));
    }
}
