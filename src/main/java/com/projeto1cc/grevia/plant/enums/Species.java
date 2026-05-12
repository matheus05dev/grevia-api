package com.projeto1cc.grevia.plant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Species {
    // Ornamentais — maturityDays = tempo até fase adulta
    ESPADA_DE_SAO_JORGE(PlantUtility.ORNAMENTAL, 120),
    SAMAMBAIA(PlantUtility.ORNAMENTAL, 90),
    SUCULENTA(PlantUtility.ORNAMENTAL, 150),
    CACTO(PlantUtility.ORNAMENTAL, 180),
    COSTELA_DE_ADAO(PlantUtility.ORNAMENTAL, 180),
    JIBOIA(PlantUtility.ORNAMENTAL, 120),
    ZAMIOCULCA(PlantUtility.ORNAMENTAL, 150),
    LIRIO_DA_PAZ(PlantUtility.ORNAMENTAL, 120),
    ORQUIDEA(PlantUtility.ORNAMENTAL, 180),
    ROSA(PlantUtility.ORNAMENTAL, 120),
    LAVANDA(PlantUtility.ORNAMENTAL, 120),

    // Hortaliças / Saladas — maturityDays = dias até colheita
    ALFACE(PlantUtility.HORTALICA_SALADA, 40),
    RUCULA(PlantUtility.HORTALICA_SALADA, 35),
    ESPINAFRE(PlantUtility.HORTALICA_SALADA, 45),
    COUVE(PlantUtility.HORTALICA_SALADA, 75),
    ALMEIRAO(PlantUtility.HORTALICA_SALADA, 50),
    ACELGA(PlantUtility.HORTALICA_SALADA, 60),
    MOSTARDA(PlantUtility.HORTALICA_SALADA, 40),
    ENDIVIA(PlantUtility.HORTALICA_SALADA, 50),
    AGRIAO(PlantUtility.HORTALICA_SALADA, 40),
    CHICORIA(PlantUtility.HORTALICA_SALADA, 50),

    // Temperos / Ervas
    CEBOLINHA(PlantUtility.TEMPERO_ERVA, 45),
    SALSINHA(PlantUtility.TEMPERO_ERVA, 65),
    MANJERICAO(PlantUtility.TEMPERO_ERVA, 50),
    HORTELA(PlantUtility.TEMPERO_ERVA, 50),
    COENTRO(PlantUtility.TEMPERO_ERVA, 38),
    FUNCHO(PlantUtility.TEMPERO_ERVA, 80),
    TOMILHO(PlantUtility.TEMPERO_ERVA, 60),
    ALECRIM(PlantUtility.TEMPERO_ERVA, 90),
    OREGANO(PlantUtility.TEMPERO_ERVA, 60),
    CAPIM_LIMAO(PlantUtility.TEMPERO_ERVA, 90),
    LOURO(PlantUtility.TEMPERO_ERVA, 180),
    STEVIA(PlantUtility.TEMPERO_ERVA, 90),
    MELISSA(PlantUtility.TEMPERO_ERVA, 60),
    SALVIA(PlantUtility.TEMPERO_ERVA, 60),

    // Frutas
    TOMATE(PlantUtility.FRUTA, 80),
    MORANGO(PlantUtility.FRUTA, 90),
    PIMENTA(PlantUtility.FRUTA, 90),
    TOMATE_CEREJA(PlantUtility.FRUTA, 80),
    PIMENTAO(PlantUtility.FRUTA, 90),
    PEPINO(PlantUtility.FRUTA, 70),
    MARACUJA(PlantUtility.FRUTA, 120),
    MELANCIA_MINI(PlantUtility.FRUTA, 90),
    ABOBRINHA(PlantUtility.FRUTA, 60),
    BERINJELA(PlantUtility.FRUTA, 90),
    QUIABO(PlantUtility.FRUTA, 60),

    // Legumes / Raízes
    CENOURA(PlantUtility.LEGUME_RAIZ, 75),
    BETERRABA(PlantUtility.LEGUME_RAIZ, 65),
    RABANETE(PlantUtility.LEGUME_RAIZ, 30),
    ALHO(PlantUtility.LEGUME_RAIZ, 105),
    CEBOLA(PlantUtility.LEGUME_RAIZ, 105),
    NABO(PlantUtility.LEGUME_RAIZ, 50),
    GENGIBRE(PlantUtility.LEGUME_RAIZ, 180),
    CURCUMA(PlantUtility.LEGUME_RAIZ, 200),
    BATATA(PlantUtility.LEGUME_RAIZ, 100),
    BATATA_DOCE(PlantUtility.LEGUME_RAIZ, 120),
    AMENDOIM(PlantUtility.LEGUME_RAIZ, 120),

    // Outros
    ERVILHA(PlantUtility.OUTREM, 65),
    FEIJAO(PlantUtility.OUTREM, 65),
    FEIJAO_VAGEM(PlantUtility.OUTREM, 65),
    MILHO_ANAO(PlantUtility.OUTREM, 90),
    OUTRA(PlantUtility.OUTREM, 90);

    private final PlantUtility utility;
    private final int maturityDays;
}
