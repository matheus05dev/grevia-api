package com.projeto1cc.grevia.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GardenerLevel {
    INICIANTE(1, "Jardineiro Iniciante", "🌱", 0),
    APRENDIZ(2, "Jardineiro Aprendiz", "🌿", 50),
    DEDICADO(3, "Jardineiro Dedicado", "🪴", 200),
    MESTRE(4, "Mestre Botânico", "🌳", 500);

    private final int level;
    private final String title;
    private final String emoji;
    private final int minPoints;

    public String getFullTitle() {
        return emoji + " " + title;
    }

    public static GardenerLevel fromPoints(int points) {
        GardenerLevel result = INICIANTE;
        for (GardenerLevel lvl : values()) {
            if (points >= lvl.minPoints) {
                result = lvl;
            }
        }
        return result;
    }
}
