package com.projeto1cc.grevia.plant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SoilType {
    ARENOSO("Arenoso"),
    ARGILOSO("Argiloso"),
    HUMOSO("Humoso"),
    CALCARIO("Calcário"),
    MISTO("Misto");

    private final String displayName;
}
