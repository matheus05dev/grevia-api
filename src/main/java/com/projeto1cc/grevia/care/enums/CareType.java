package com.projeto1cc.grevia.care.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CareType {
    REGA("Rega"),
    PODA("Poda"),
    ADUBACAO("Adubação"),
    TRANSPLANTE("Transplante"),
    CONTROLE_PRAGAS("Controle de Pragas"),
    OUTRO("Outro");

    private final String displayName;
}
