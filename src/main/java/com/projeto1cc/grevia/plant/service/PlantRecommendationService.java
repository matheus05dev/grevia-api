package com.projeto1cc.grevia.plant.service;

import com.projeto1cc.grevia.plant.enums.SoilType;
import com.projeto1cc.grevia.plant.enums.Species;
import org.springframework.stereotype.Service;

@Service
public class PlantRecommendationService {

    public String generateRecommendation(SoilType soilType, Species species) {
        String speciesRec = getSpeciesRecommendation(species);
        String soilRec    = getSoilRecommendation(soilType);
        String combined   = getCombinedAdvice(species, soilType);

        if (species == Species.OUTRA) {
            return speciesRec + " " + soilRec
                    + " Como esta é uma espécie personalizada, pesquise a frequência ideal de rega e adubação"
                    + " e cadastre seus próprios planos de cuidado no aplicativo!";
        }

        return speciesRec + " " + soilRec + (combined.isEmpty() ? "" : " " + combined);
    }

    // -------------------------------------------------------------------------
    // Recomendações por espécie (perfil geral da planta)
    // -------------------------------------------------------------------------
    private String getSpeciesRecommendation(Species species) {
        if (species == null) {
            return "Planta não identificada: aplique cuidados gerais de rega moderada e boa luminosidade.";
        }
        return switch (species) {
            case CACTO ->
                "🌵 Cacto: planta altamente resistente à seca. Regue apenas quando o solo estiver completamente seco"
                + " (a cada 2–4 semanas no verão, 1x por mês ou menos no inverno). Priorize luz direta por ao menos 4h/dia."
                + " Evite encharcar — o maior inimigo do cacto é o excesso de água.";
            case SUCULENTA ->
                "🪴 Suculenta: armazena água nas folhas, por isso suporta bem a seca."
                + " Regue a cada 10–15 dias no verão e a cada 3–4 semanas no inverno, sempre que o solo estiver seco."
                + " Prefere luz intensa e boa ventilação. Folhas murchas indicam sedento; folhas amarelas e moles indicam excesso de água.";
            case ESPADA_DE_SAO_JORGE ->
                "🌿 Espada-de-São-Jorge: uma das plantas mais resistentes, tolera ambientes com pouca luz e esquecimentos na rega."
                + " Regue a cada 15 dias (quinzenal), deixando o solo secar completamente entre as regas."
                + " Evite luz solar direta intensa — meia-sombra é o ideal. Adube a cada 2 meses em pequena quantidade.";
            case ZAMIOCULCA ->
                "🌱 Zamioculca: tolera baixa luminosidade e rega irregular."
                + " Regue apenas quando os 3–4 cm superiores do solo estiverem completamente secos (a cada 2–3 semanas)."
                + " Evite luz direta e mantê-la encharcada — suas raízes bulbosas acumulam água naturalmente.";
            case JIBOIA ->
                "🍃 Jiboia: planta tropical de interior muito adaptável. Regue quando a camada superficial do solo (2–3 cm)"
                + " estiver seca, geralmente a cada 7–10 dias. Prefere luminosidade indireta e alta umidade."
                + " Borrife água nas folhas em dias secos para simular o habitat úmido natural dela.";
            case SAMAMBAIA ->
                "🌿 Samambaia: ama umidade e não suporta seca. Regue com frequência para manter o solo sempre levemente úmido,"
                + " mas nunca encharcado. Prefere sombra ou meia-sombra, com boa ventilação."
                + " Borrife as folhas regularmente — ambiente seco resseca suas pontas.";
            case COSTELA_DE_ADAO ->
                "🌴 Costela-de-Adão: tropical de grande porte que adora umidade e luz indireta brilhante."
                + " Regue quando os 3–4 cm superiores do solo secarem (aprox. 1x por semana no verão)."
                + " Limpe as folhas grandes com um pano úmido para facilitar a fotossíntese.";
            case LIRIO_DA_PAZ ->
                "🌸 Lírio-da-paz: planta de interior que tolera sombra, mas floresce melhor com luz indireta."
                + " Regue quando as folhas começarem a ficar levemente murchas — ela vive bem com menos água do que parece."
                + " Mantenha longe de correntes de ar frio e nunca deixe o vaso com pires cheio d'água.";
            case ORQUIDEA ->
                "🌺 Orquídea: não gosta de terra convencional — prefere substrato próprio (casca de pinus, carvão)."
                + " Regue 1x por semana imergindo o vaso em água por 15 min e deixe escorrer completamente."
                + " Prefere luz clara indireta e boa ventilação. Adube com fertilizante específico bimestral diluído a 1/4 da dose."
                + " Faça controle preventivo de pragas (cochonilhas, ácaros) a cada 15 dias inspecionando as folhas.";
            case ROSA ->
                "🌹 Rosa: precisa de sol pleno (mínimo 6h/dia) e rega regular de 1x por semana, mantendo o solo levemente úmido."
                + " Regue na base, evitando molhar as folhas para prevenir fungos. Adube mensalmente com fertilizante NPK equilibrado."
                + " Realize podas mensais de flores secas e faça controle preventivo de pragas a cada 15 dias.";
            case TOMATE ->
                "🍅 Tomate: cultura exigente que precisa de sol direto pleno (8h/dia), rega regular e consistente."
                + " Regue diariamente em dias quentes, mantendo o solo uniformemente úmido para evitar rachadura dos frutos."
                + " Tutore as hastes, remova brotos laterais (\"filhos\") e adube com fósforo e potássio na fase de frutificação.";
            case ALFACE ->
                "🥬 Alface: prefere clima ameno, dias de temperatura abaixo de 25°C e boa luminosidade (4–6h de sol)."
                + " Regue diariamente ou a cada 2 dias para manter o solo úmido mas nunca encharcado."
                + " Evite pleno sol em horários de pico no verão — leve sombreamento preserva as folhas e evita florescimento precoce.";
            case CEBOLINHA ->
                "🌿 Cebolinha: fácil de cultivar, prefere solo fértil e rega regular (a cada 2–3 dias)."
                + " Requer pelo menos 4–6h de luz solar por dia. Colha as folhas a partir de 15 cm de altura,"
                + " sempre deixando a base para rebrotar. Adube com nitrogênio a cada 30 dias para folhas vigorosas.";
            case SALSINHA ->
                "🌿 Salsinha: cultive em local com boa luminosidade (6h/dia) e regue regularmente para manter o solo levemente úmido."
                + " Colha os galhos externos primeiro para estimular o crescimento do centro."
                + " Evite o florescimento (planta \"boltar\"): se aparecerem flores, remova-as para prolongar a produção de folhas.";
            case MANJERICAO ->
                "🌿 Manjericão: planta de clima quente que ama sol pleno (6–8h/dia) e rega frequente."
                + " Mantenha o solo úmido, mas com boa drenagem. Remova flores assim que surgirem para prolongar a vida da planta."
                + " Colha as folhas pelo topo para estimular o crescimento lateral e deixar a planta mais densa.";
            case HORTELA ->
                "🌿 Hortelã: cresce de forma vigorosa e pode invadir o jardim — cultive de preferência em vasos."
                + " Prefere luz parcial (4–6h de sol) e solo sempre úmido. Regue regularmente (a cada 1–2 dias)."
                + " Colha as pontas para estimular o crescimento. Tolera divisão das raízes para propagação fácil.";
            case MORANGO ->
                "🍓 Morango: prefere sol pleno (8h/dia) e solo fértil com boa drenagem."
                + " Regue regularmente (a cada 1–2 dias), mantendo solo úmido mas nunca encharcado."
                + " Coloque palha (mulching) ao redor das plantas para conservar umidade e proteger os frutos do solo."
                + " Remova as estolhas (runners) para concentrar energia nos frutos.";
            case PIMENTA ->
                "🌶️ Pimenta: cultura tropical que ama calor e sol pleno (8h/dia)."
                + " Regue regularmente mas com moderação (a cada 2–3 dias), pois tolera levemente a seca mas não o encharcamento."
                + " Adube com fósforo e potássio para estimular a frutificação. Colha quando atingir a cor desejada.";
            case OUTRA ->
                "🌱 Espécie personalizada: aplique cuidados básicos e pesquise as necessidades específicas desta planta.";
        };
    }

    // -------------------------------------------------------------------------
    // Recomendações por tipo de solo (condicionamento e manejo)
    // -------------------------------------------------------------------------
    private String getSoilRecommendation(SoilType soilType) {
        if (soilType == null) {
            return "Solo não especificado: use substrato universal bem drenado como ponto de partida.";
        }
        return switch (soilType) {
            case ARENOSO ->
                "🪨 Solo arenoso: drena água muito rapidamente, o que exige regas mais frequentes e em menor volume."
                + " Dica caseira: misture húmus de minhoca ou composto orgânico para aumentar a retenção de água e nutrir o solo.";
            case ARGILOSO ->
                "🪵 Solo argiloso: retém bastante água e pode encharcar com facilidade, compactando as raízes."
                + " Regue com menos frequência, mas em maior volume. Dica caseira: incorpore areia grossa ou perlita"
                + " para soltar a estrutura e melhorar a drenagem.";
            case HUMOSO ->
                "🌱 Solo humoso: rico em matéria orgânica, excelente retenção de umidade e nutrientes."
                + " Dica caseira: reforce os nutrientes a cada 2 meses adicionando borra de café fina (nitrogênio)"
                + " e cascas de ovo trituradas (cálcio) por cima da terra.";
            case CALCARIO ->
                "⚪ Solo calcário: altamente alcalino, pode bloquear a absorção de ferro e manganês causando folhas amarelas."
                + " Dica caseira: use borra de café usada (lavada) para acidificar levemente"
                + " e fornecer magnésio; monitore o pH se possível.";
            case MISTO ->
                "🌍 Solo misto: oferece um bom equilíbrio entre drenagem e retenção de nutrientes."
                + " Dica caseira: para manter a fertilidade, prepare um adubo líquido caseiro batendo casca de banana"
                + " (rica em potássio) com água no liquidificador e use na rega 1x por mês.";
        };
    }

    // -------------------------------------------------------------------------
    // Conselho combinado (compatibilidade espécie + solo)
    // -------------------------------------------------------------------------
    private String getCombinedAdvice(Species species, SoilType soilType) {
        if (species == null || soilType == null || species == Species.OUTRA) {
            return "";
        }

        // Grupos de espécies por tolerância hídrica
        boolean isXerophyte  = species == Species.CACTO || species == Species.SUCULENTA
                || species == Species.ESPADA_DE_SAO_JORGE || species == Species.ZAMIOCULCA;
        boolean isTropical   = species == Species.JIBOIA || species == Species.SAMAMBAIA
                || species == Species.COSTELA_DE_ADAO || species == Species.LIRIO_DA_PAZ;
        boolean isHerb       = species == Species.CEBOLINHA || species == Species.SALSINHA
                || species == Species.MANJERICAO || species == Species.HORTELA;
        boolean isVegetable  = species == Species.TOMATE || species == Species.ALFACE
                || species == Species.MORANGO || species == Species.PIMENTA;

        return switch (soilType) {
            case ARENOSO -> {
                if (isXerophyte)
                    yield "✅ Compatibilidade ótima: espécies resistentes à seca se adaptam muito bem ao solo arenoso de alta drenagem."
                            + " Apenas tome cuidado para não deixar o solo completamente seco por longos períodos em dias de muito calor.";
                else if (isTropical)
                    yield "⚠️ Atenção: plantas tropicais precisam de umidade constante, mas o solo arenoso drena rápido."
                            + " Aumente a frequência de rega e adicione matéria orgânica ao substrato para compensar.";
                else if (isHerb || isVegetable)
                    yield "⚠️ Atenção: ervas e hortaliças demandam nutrientes e umidade que o solo arenoso não retém bem."
                            + " Enriqueça com composto orgânico e aumente a frequência de rega e adubação.";
                else
                    yield "⚠️ Considere enriquecer o solo arenoso com matéria orgânica para suprir melhor as necessidades desta planta.";
            }
            case ARGILOSO -> {
                if (isXerophyte)
                    yield "⛔ Incompatibilidade: cactos, suculentas e espécies resistentes à seca sofrem bastante em solo argiloso"
                            + " que retém muita água. Misture pelo menos 50% de areia grossa ou perlita ao substrato para garantir drenagem adequada.";
                else if (isTropical)
                    yield "⚠️ Solo argiloso retém umidade que plantas tropicais apreciam, mas cuidado com o encharcamento."
                            + " Garanta buracos de drenagem no vaso e revise o solo periodicamente para evitar compactação.";
                else if (isVegetable)
                    yield "⚠️ Hortaliças precisam de raízes soltas para crescer bem. No solo argiloso, incorpore composto"
                            + " e areia para aerar o substrato e facilitar o desenvolvimento radicular.";
                else
                    yield "⚠️ Solo argiloso pode compactar e sufocar raízes. Solte o substrato com areia grossa e certifique-se de boa drenagem no vaso.";
            }
            case HUMOSO -> {
                if (isXerophyte)
                    yield "⚠️ Solo humoso retém muito mais umidade do que cactos e suculentas toleram."
                            + " Misture areia ou perlita (proporção 1:1) ao húmus para garantir que o excesso de água escorra rapidamente.";
                else if (isTropical || isHerb || isVegetable)
                    yield "✅ Combinação excelente: o solo humoso fornece umidade e nutrientes ideais para esta espécie."
                            + " Mantenha a adubação orgânica regular e você terá uma planta saudável e produtiva.";
                else
                    yield "✅ Solo humoso é bastante nutritivo e favorável para a maioria das espécies.";
            }
            case CALCARIO -> {
                if (isXerophyte)
                    yield "✅ Cactos e espécies resistentes à seca têm boa tolerância a solos alcalinos como o calcário."
                            + " Ainda assim, monitore o pH e adicione matéria orgânica ácida (como borra de café) se surgirem folhas amarelas.";
                else if (isTropical)
                    yield "⛔ Plantas tropicais geralmente preferem solos levemente ácidos a neutros. O pH alcalino do solo calcário"
                            + " pode bloquear nutrientes essenciais. Corrija com enxofre agrícola ou borra de café e considere trocar ou misturar o substrato.";
                else if (isVegetable || isHerb)
                    yield "⛔ Hortaliças e ervas têm dificuldade em absorver ferro e zinco em solos muito alcalinos."
                            + " Corrija o pH com matéria orgânica ácida antes de plantar. Acompanhe o desenvolvimento e folhas amarelas são sinal de alerta.";
                else
                    yield "⚠️ Solo calcário é alcalino e pode dificultar a absorção de nutrientes. Monitore sinais de carência (folhas amarelas) e corrija com matéria orgânica.";
            }
            case MISTO -> {
                if (isXerophyte)
                    yield "✅ Solo misto com componentes de drenagem atende bem espécies resistentes à seca."
                            + " Verifique se a proporção permite que o solo seque rapidamente entre as regas.";
                else
                    yield "✅ Solo misto oferece bom equilíbrio para esta espécie. Mantenha adubações periódicas para repor nutrientes"
                            + " e verifique a drenagem do vaso regularmente.";
            }
        };
    }
}
