package com.projeto1cc.grevia.plant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlantUtility {
    ORNAMENTAL("Ornamental"),
    HORTALICA_SALADA("Hortaliça / Salada"),
    TEMPERO_ERVA("Tempero e Erva"),
    FRUTA("Fruta"),
    LEGUME_RAIZ("Legume / Raiz"),
    OUTREM("Outro");

    private final String displayName;
}
