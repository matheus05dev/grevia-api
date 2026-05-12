package com.projeto1cc.grevia.plant.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpeciesTest {

    @Test
    void allSpecies_ShouldHavePositiveMaturityDays() {
        for (Species species : Species.values()) {
            assertTrue(species.getMaturityDays() > 0,
                species.name() + " deve ter maturityDays > 0");
        }
    }

    @Test
    void allSpecies_ShouldHaveNonNullUtility() {
        for (Species species : Species.values()) {
            assertNotNull(species.getUtility(),
                species.name() + " deve ter utility não nulo");
        }
    }

    @Test
    void specificSpecies_ShouldHaveCorrectValues() {
        assertEquals(PlantUtility.ORNAMENTAL, Species.CACTO.getUtility());
        assertEquals(180, Species.CACTO.getMaturityDays());

        assertEquals(PlantUtility.HORTALICA_SALADA, Species.ALFACE.getUtility());
        assertEquals(40, Species.ALFACE.getMaturityDays());

        assertEquals(PlantUtility.TEMPERO_ERVA, Species.MANJERICAO.getUtility());
        assertEquals(50, Species.MANJERICAO.getMaturityDays());

        assertEquals(PlantUtility.FRUTA, Species.TOMATE.getUtility());
        assertEquals(80, Species.TOMATE.getMaturityDays());

        assertEquals(PlantUtility.LEGUME_RAIZ, Species.CENOURA.getUtility());
        assertEquals(75, Species.CENOURA.getMaturityDays());
    }

    @Test
    void displayNames_ShouldBeNonEmpty() {
        for (PlantUtility utility : PlantUtility.values()) {
            assertNotNull(utility.getDisplayName());
            assertFalse(utility.getDisplayName().isEmpty(),
                utility.name() + " displayName não deve ser vazio");
        }
    }

    @Test
    void soilType_ShouldHaveCorrectDisplayNames() {
        assertEquals("Arenoso", SoilType.ARENOSO.getDisplayName());
        assertEquals("Humoso", SoilType.HUMOSO.getDisplayName());
        assertEquals("Calcário", SoilType.CALCARIO.getDisplayName());
        assertEquals("Misto", SoilType.MISTO.getDisplayName());
    }
}
