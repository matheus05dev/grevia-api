package com.projeto1cc.grevia.care.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FrequencyType {
    DIARIO("Diário"),
    SEMANAL("Semanal"),
    DUAS_VEZES_SEMANA("2x por Semana"),
    TRES_VEZES_SEMANA("3x por Semana"),
    QUINZENAL("Quinzenal"),
    MENSAL("Mensal"),
    BIMESTRAL("Bimestral"),
    SOB_DEMANDA("Sob Demanda");

    private final String displayName;
}
