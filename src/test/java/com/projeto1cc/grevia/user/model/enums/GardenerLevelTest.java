package com.projeto1cc.grevia.user.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GardenerLevelTest {

    @Test
    void fromPoints_ShouldReturnIniciante_WhenPointsAreZero() {
        assertEquals(GardenerLevel.INICIANTE, GardenerLevel.fromPoints(0));
    }

    @Test
    void fromPoints_ShouldReturnIniciante_WhenPointsAreBelow50() {
        assertEquals(GardenerLevel.INICIANTE, GardenerLevel.fromPoints(49));
    }

    @Test
    void fromPoints_ShouldReturnAprendiz_WhenPointsAre50() {
        assertEquals(GardenerLevel.APRENDIZ, GardenerLevel.fromPoints(50));
    }

    @Test
    void fromPoints_ShouldReturnAprendiz_WhenPointsAreBetween50And199() {
        assertEquals(GardenerLevel.APRENDIZ, GardenerLevel.fromPoints(150));
    }

    @Test
    void fromPoints_ShouldReturnDedicado_WhenPointsAre200() {
        assertEquals(GardenerLevel.DEDICADO, GardenerLevel.fromPoints(200));
    }

    @Test
    void fromPoints_ShouldReturnMestre_WhenPointsAre500() {
        assertEquals(GardenerLevel.MESTRE, GardenerLevel.fromPoints(500));
    }

    @Test
    void fromPoints_ShouldReturnMestre_WhenPointsAreVeryHigh() {
        assertEquals(GardenerLevel.MESTRE, GardenerLevel.fromPoints(9999));
    }

    @Test
    void getFullTitle_ShouldCombineEmojiAndTitle() {
        assertEquals("🌱 Jardineiro Iniciante", GardenerLevel.INICIANTE.getFullTitle());
        assertEquals("🌳 Mestre Botânico", GardenerLevel.MESTRE.getFullTitle());
    }

    @Test
    void getLevel_ShouldReturnCorrectLevelNumber() {
        assertEquals(1, GardenerLevel.INICIANTE.getLevel());
        assertEquals(2, GardenerLevel.APRENDIZ.getLevel());
        assertEquals(3, GardenerLevel.DEDICADO.getLevel());
        assertEquals(4, GardenerLevel.MESTRE.getLevel());
    }

    @Test
    void getMinPoints_ShouldReturnCorrectThresholds() {
        assertEquals(0, GardenerLevel.INICIANTE.getMinPoints());
        assertEquals(50, GardenerLevel.APRENDIZ.getMinPoints());
        assertEquals(200, GardenerLevel.DEDICADO.getMinPoints());
        assertEquals(500, GardenerLevel.MESTRE.getMinPoints());
    }
}
