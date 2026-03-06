package com.projeto1cc.grevia.plant.service;

import com.projeto1cc.grevia.plant.enums.SoilType;
import org.springframework.stereotype.Service;

@Service
public class PlantRecommendationService {

    public String generateRecommendation(SoilType soilType, com.projeto1cc.grevia.plant.enums.Species species) {
        String baseRec = getSoilRecommendation(soilType);
        
        if (species == com.projeto1cc.grevia.plant.enums.Species.OUTRA) {
            return baseRec + " Como esta é uma espécie personalizada, recomendamos pesquisar a frequência ideal de rega e adubação para cadastrar seus próprios planos de cuidado no aplicativo!";
        }
        
        return baseRec;
    }

    private String getSoilRecommendation(SoilType soilType) {
        if (soilType == null) {
            return "Cuidados gerais: garanta rega moderada e luz solar adequada.";
        }

        return switch (soilType) {
            case ARENOSO -> "Solo arenoso drena rapidamente. Regue com mais frequência, mas com menos volume. Dica caseira: misture húmus de minhoca ou compostagem para ajudar a reter água e nutrientes.";
            case ARGILOSO -> "Solo argiloso retém bastante água. Regue em maior quantidade, mas com menos frequência para evitar o apodrecimento das raízes. Dica caseira: adicione areia grossa ou perlita misturada na terra para soltar mais as raízes.";
            case HUMOSO -> "Solo rico em húmus é excelente, retendo bem umidade e nutrientes. Dica caseira: reforce a nutrição a cada 2 meses triturando cascas de ovo (ricas em cálcio) e borra de café fina (rica em nitrogênio) por cima da terra.";
            case CALCARIO -> "Solo calcário é altamente alcalino. Pode dificultar a absorção de nutrientes. Dica caseira: se as folhas amarelarem, use borra de café usada (lavada) que ajuda a acidificar levemente e fornecer magnésio livre.";
            case MISTO -> "Solo misto normalmente oferece um bom equilíbrio de drenagem e retenção. Dica caseira: para manter a planta forte, você pode bater casca de banana (rica em potássio) com água no liquidificador e usar na rega 1x ao mês.";
        };
    }
}
