package com.projeto1cc.grevia.plant.service;

import com.projeto1cc.grevia.plant.enums.SoilType;
import org.springframework.stereotype.Service;

@Service
public class PlantRecommendationService {

    public String generateRecommendation(SoilType soilType) {
        if (soilType == null) {
            return "Cuidados gerais: garanta rega moderada e luz solar adequada.";
        }

        return switch (soilType) {
            case ARENOSO -> "Solo arenoso drena rapidamente. Regue com mais frequência, mas com menos volume. Precisa de adubação mais constante, pois os nutrientes são lavados facilmente.";
            case ARGILOSO -> "Solo argiloso retém bastante água. Regue em maior quantidade, mas com menos frequência para evitar o apodrecimento das raízes. Evite pisar quando estiver molhado para não compactar.";
            case HUMOSO -> "Solo rico em húmus é excelente para a maioria das plantas, retendo bem a umidade e os nutrientes. Mantenha com adições regulares de composto orgânico.";
            case CALCARIO -> "Solo calcário é altamente alcalino. Escolha plantas que tolerem essas condições ou corrija com matéria orgânica ácida.";
            case MISTO -> "Solo misto normalmente oferece um bom equilíbrio de drenagem e retenção. Monitore a umidade verificando a camada superior do solo antes de regar.";
        };
    }
}
