package com.projeto1cc.grevia.plant.enums;

public enum Species {
    ESPADA_DE_SAO_JORGE(PlantUtility.ORNAMENTAL),
    SAMAMBAIA(PlantUtility.ORNAMENTAL),
    SUCULENTA(PlantUtility.ORNAMENTAL),
    CACTO(PlantUtility.ORNAMENTAL),
    COSTELA_DE_ADAO(PlantUtility.ORNAMENTAL),
    JIBOIA(PlantUtility.ORNAMENTAL),
    ZAMIOCULCA(PlantUtility.ORNAMENTAL),
    LIRIO_DA_PAZ(PlantUtility.ORNAMENTAL),
    ORQUIDEA(PlantUtility.ORNAMENTAL),
    ROSA(PlantUtility.ORNAMENTAL),
    LAVANDA(PlantUtility.ORNAMENTAL),
    
    ALFACE(PlantUtility.HORTALICA_SALADA),
    RUCULA(PlantUtility.HORTALICA_SALADA),
    ESPINAFRE(PlantUtility.HORTALICA_SALADA),
    COUVE(PlantUtility.HORTALICA_SALADA),
    ALMEIRAO(PlantUtility.HORTALICA_SALADA),
    ACELGA(PlantUtility.HORTALICA_SALADA),
    MOSTARDA(PlantUtility.HORTALICA_SALADA),
    ENDIVIA(PlantUtility.HORTALICA_SALADA),
    AGRIAO(PlantUtility.HORTALICA_SALADA),
    CHICORIA(PlantUtility.HORTALICA_SALADA),

    CEBOLINHA(PlantUtility.TEMPERO_ERVA),
    SALSINHA(PlantUtility.TEMPERO_ERVA),
    MANJERICAO(PlantUtility.TEMPERO_ERVA),
    HORTELA(PlantUtility.TEMPERO_ERVA),
    COENTRO(PlantUtility.TEMPERO_ERVA),
    FUNCHO(PlantUtility.TEMPERO_ERVA),
    TOMILHO(PlantUtility.TEMPERO_ERVA),
    ALECRIM(PlantUtility.TEMPERO_ERVA),
    OREGANO(PlantUtility.TEMPERO_ERVA),
    CAPIM_LIMAO(PlantUtility.TEMPERO_ERVA),
    LOURO(PlantUtility.TEMPERO_ERVA),
    STEVIA(PlantUtility.TEMPERO_ERVA),
    MELISSA(PlantUtility.TEMPERO_ERVA),
    SALVIA(PlantUtility.TEMPERO_ERVA),

    TOMATE(PlantUtility.FRUTA),
    MORANGO(PlantUtility.FRUTA),
    PIMENTA(PlantUtility.FRUTA),
    TOMATE_CEREJA(PlantUtility.FRUTA),
    PIMENTAO(PlantUtility.FRUTA),
    PEPINO(PlantUtility.FRUTA),
    MARACUJA(PlantUtility.FRUTA),
    MELANCIA_MINI(PlantUtility.FRUTA),
    ABOBRINHA(PlantUtility.FRUTA),
    BERINJELA(PlantUtility.FRUTA),
    QUIABO(PlantUtility.FRUTA),

    CENOURA(PlantUtility.LEGUME_RAIZ),
    BETERRABA(PlantUtility.LEGUME_RAIZ),
    RABANETE(PlantUtility.LEGUME_RAIZ),
    ALHO(PlantUtility.LEGUME_RAIZ),
    CEBOLA(PlantUtility.LEGUME_RAIZ),
    NABO(PlantUtility.LEGUME_RAIZ),
    GENGIBRE(PlantUtility.LEGUME_RAIZ),
    CURCUMA(PlantUtility.LEGUME_RAIZ),
    BATATA(PlantUtility.LEGUME_RAIZ),
    BATATA_DOCE(PlantUtility.LEGUME_RAIZ),
    AMENDOIM(PlantUtility.LEGUME_RAIZ),

    ERVILHA(PlantUtility.OUTREM),
    FEIJAO(PlantUtility.OUTREM),
    FEIJAO_VAGEM(PlantUtility.OUTREM),
    MILHO_ANAO(PlantUtility.OUTREM),
    OUTRA(PlantUtility.OUTREM);

    private final PlantUtility utility;

    Species(PlantUtility utility) {
        this.utility = utility;
    }

    public PlantUtility getUtility() {
        return utility;
    }
}
