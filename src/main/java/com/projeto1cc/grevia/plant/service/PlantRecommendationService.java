package com.projeto1cc.grevia.plant.service;

import com.projeto1cc.grevia.plant.enums.SoilType;
import com.projeto1cc.grevia.plant.enums.Species;
import org.springframework.stereotype.Service;

@Service
public class PlantRecommendationService {

    public String generateRecommendation(SoilType soilType, Species species) {
        String utilityStr = getUtilityDisplayName(species);
        String speciesRec = getSpeciesRecommendation(species);
        String soilRec    = getSoilRecommendation(soilType);
        String combined   = getCombinedAdvice(species, soilType);

        String prefix = utilityStr.isEmpty() ? "" : "💡 Uso Comum: " + utilityStr + "\n";

        if (species == Species.OUTRA) {
            return prefix + speciesRec + " " + soilRec
                    + " Como esta é uma espécie personalizada, pesquise a frequência ideal de rega e adubação"
                    + " e cadastre seus próprios planos de cuidado no aplicativo!";
        }

        return prefix + speciesRec + " " + soilRec + (combined.isEmpty() ? "" : " " + combined);
    }

    private String getUtilityDisplayName(Species species) {
        if (species == null || species.getUtility() == null) return "";
        return switch (species.getUtility()) {
            case ORNAMENTAL -> "Ornamental / Decoração";
            case HORTALICA_SALADA -> "Hortaliça / Salada";
            case TEMPERO_ERVA -> "Tempero / Erva Aromática";
            case FRUTA -> "Fruta / Fruto";
            case LEGUME_RAIZ -> "Legume / Raiz / Tubérculo";
            case OUTREM -> "Outros (Grãos, Leguminosas, etc)";
        };
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
            case TOMATE -> "🍅 Tomate: Área mínima: Vaso 15 L. Colheita: 70–90 dias. Adubo: Composto + potássio. Rega: 1x dia. Dica: Use tutor.";
            case ALFACE -> "🥬 Alface: Área mínima: Vaso 2–3 L. Colheita: 30–45 dias. Adubo: Húmus de minhoca. Rega: 1x por dia. Dica: Prefere meia-sombra em clima quente.";
            case CEBOLINHA -> "🌿 Cebolinha: Área mínima: Vaso 2 L. Colheita: 30–60 dias. Adubo: Húmus. Rega: 3x semana. Dica: Corte acima da base.";
            case SALSINHA -> "🌿 Salsinha: Área mínima: Vaso 2 L. Colheita: 60–70 dias. Adubo: Composto. Rega: 3x semana. Dica: Germinação lenta.";
            case MANJERICAO -> "🌿 Manjericão: Área mínima: Vaso 3 L. Colheita: 40–60 dias. Adubo: Húmus. Rega: 3x semana. Dica: Podar pontas.";
            case HORTELA -> "🌿 Hortelã: Área mínima: Vaso 3 L. Colheita: 40–60 dias. Adubo: Composto. Rega: 3–4x semana. Dica: Espalha rápido.";
            case MORANGO -> "🍓 Morango: Área mínima: Vaso 5 L. Colheita: 60–90 dias. Adubo: Húmus. Rega: 1x dia. Dica: Boa drenagem.";
            case PIMENTA -> "🌶️ Pimenta: Área mínima: Vaso 10 L. Colheita: 80–100 dias. Adubo: Húmus. Rega: 1x dia. Dica: Colher quando maduro.";
            case RUCULA -> "🥬 Rúcula: Área mínima: Vaso 2 L. Colheita: 30–40 dias. Adubo: Composto orgânico. Rega: 1x por dia. Dica: Colheita de folhas jovens.";
            case ESPINAFRE -> "🥬 Espinafre: Área mínima: Vaso 3 L. Colheita: 40–50 dias. Adubo: Húmus. Rega: 1x por dia. Dica: Solo sempre úmido.";
            case COUVE -> "🥬 Couve: Área mínima: Vaso 10 L. Colheita: 60–90 dias. Adubo: Composto + NPK leve. Rega: 3–4x semana. Dica: Retire folhas externas primeiro.";
            case COENTRO -> "🌿 Coentro: Área mínima: Vaso 2 L. Colheita: 30–45 dias. Adubo: Composto. Rega: 3x semana. Dica: Evitar calor extremo.";
            case TOMATE_CEREJA -> "🍅 Tomate cereja: Área mínima: Vaso 15 L. Colheita: 70–90 dias. Adubo: Composto + potássio. Rega: 1x dia. Dica: Use tutor.";
            case PIMENTAO -> "🫑 Pimentão: Área mínima: Vaso 15 L. Colheita: 80–100 dias. Adubo: Composto. Rega: 1x dia. Dica: Boa luz solar.";
            case PEPINO -> "🥒 Pepino: Área mínima: Vaso 15 L. Colheita: 60–80 dias. Adubo: Composto. Rega: 1x dia. Dica: Use treliça.";
            case ABOBRINHA -> "🥒 Abobrinha: Área mínima: 1 m². Colheita: 50–70 dias. Adubo: Composto. Rega: 1x dia. Dica: Muito sol.";
            case BERINJELA -> "🍆 Berinjela: Área mínima: Vaso 15 L. Colheita: 80–100 dias. Adubo: Composto. Rega: 1x dia. Dica: Boa drenagem.";
            case CENOURA -> "🥕 Cenoura: Área mínima: Vaso 5 L. Colheita: 70–80 dias. Adubo: Composto. Rega: 3x semana. Dica: Solo profundo.";
            case BETERRABA -> "🍠 Beterraba: Área mínima: Vaso 5 L. Colheita: 60–70 dias. Adubo: Húmus. Rega: 3x semana. Dica: Sol pleno.";
            case RABANETE -> "🌷 Rabanete: Área mínima: Vaso 2 L. Colheita: 25–35 dias. Adubo: Composto. Rega: 1x dia. Dica: Crescimento rápido.";
            case ALHO -> "🧄 Alho: Área mínima: Vaso 5 L. Colheita: 90–120 dias. Adubo: Composto. Rega: 3x semana. Dica: Plantar dentes.";
            case CEBOLA -> "🧅 Cebola: Área mínima: Vaso 5 L. Colheita: 90–120 dias. Adubo: Composto. Rega: 3x semana. Dica: Boa luminosidade.";
            case ERVILHA -> "🫛 Ervilha: Área mínima: Vaso 10 L. Colheita: 60–70 dias. Adubo: Composto. Rega: 3x semana. Dica: Use suporte.";
            case FEIJAO -> "🫘 Feijão: Área mínima: Vaso 10 L. Colheita: 60–70 dias. Adubo: Composto. Rega: 3x semana. Dica: Plantar direto.";
            case FEIJAO_VAGEM -> "🫛 Feijão-vagem: Área mínima: Vaso 10 L. Colheita: 60–70 dias. Adubo: Composto. Rega: 3x semana. Dica: Produz bastante.";
            case MILHO_ANAO -> "🌽 Milho anão: Área mínima: 1 m². Colheita: 90 dias. Adubo: Composto. Rega: 3x semana. Dica: Plantar em grupo.";
            case BATATA -> "🥔 Batata: Área mínima: Vaso 20 L. Colheita: 90–110 dias. Adubo: Composto. Rega: 3x semana. Dica: Cobrir com terra.";
            case BATATA_DOCE -> "🍠 Batata-doce: Área mínima: 1 m². Colheita: 120 dias. Adubo: Composto. Rega: 3x semana. Dica: Clima quente.";
            case ALMEIRAO -> "🥬 Almeirão: Área mínima: Vaso 3 L. Colheita: 50 dias. Adubo: Composto. Rega: 3x semana. Dica: Folhas contínuas.";
            case ACELGA -> "🥬 Acelga: Área mínima: Vaso 5 L. Colheita: 60 dias. Adubo: Húmus. Rega: 3x semana. Dica: Tolera frio.";
            case MOSTARDA -> "🥬 Mostarda: Área mínima: Vaso 3 L. Colheita: 40 dias. Adubo: Composto. Rega: 3x semana. Dica: Cresce rápido.";
            case ENDIVIA -> "🥬 Endívia: Área mínima: Vaso 3 L. Colheita: 50 dias. Adubo: Composto. Rega: 3x semana. Dica: Prefere clima ameno.";
            case FUNCHO -> "🌿 Funcho: Área mínima: Vaso 5 L. Colheita: 80 dias. Adubo: Composto. Rega: 3x semana. Dica: Sol pleno.";
            case TOMILHO -> "🌿 Tomilho: Área mínima: Vaso 2 L. Colheita: 60 dias. Adubo: Composto leve. Rega: 2x semana. Dica: Pouca água.";
            case ALECRIM -> "🌿 Alecrim: Área mínima: Vaso 5 L. Colheita: 90 dias. Adubo: Composto leve. Rega: 2x semana. Dica: Solo drenado.";
            case OREGANO -> "🌿 Orégano: Área mínima: Vaso 3 L. Colheita: 60 dias. Adubo: Composto. Rega: 2x semana. Dica: Muito sol.";
            case CAPIM_LIMAO -> "🌿 Capim-limão: Área mínima: Vaso 10 L. Colheita: 90 dias. Adubo: Composto. Rega: 3x semana. Dica: Clima quente.";
            case AGRIAO -> "🥬 Agrião: Área mínima: Vaso 3 L. Colheita: 40 dias. Adubo: Composto. Rega: 1x dia. Dica: Gosta de umidade.";
            case CHICORIA -> "🥬 Chicória: Área mínima: Vaso 3 L. Colheita: 50 dias. Adubo: Composto. Rega: 3x semana. Dica: Sol parcial.";
            case NABO -> "🧅 Nabo: Área mínima: Vaso 5 L. Colheita: 50 dias. Adubo: Composto. Rega: 3x semana. Dica: Clima ameno.";
            case QUIABO -> "🥒 Quiabo: Área mínima: 1 m². Colheita: 60 dias. Adubo: Composto. Rega: 3x semana. Dica: Calor.";
            case AMENDOIM -> "🥜 Amendoim: Área mínima: 1 m². Colheita: 120 dias. Adubo: Composto. Rega: 3x semana. Dica: Solo solto.";
            case GENGIBRE -> "🫚 Gengibre: Área mínima: Vaso 10 L. Colheita: 180 dias. Adubo: Composto. Rega: 3x semana. Dica: Meia sombra.";
            case CURCUMA -> "🫚 Cúrcuma: Área mínima: Vaso 10 L. Colheita: 200 dias. Adubo: Composto. Rega: 3x semana. Dica: Solo úmido.";
            case LOURO -> "🌿 Louro: Área mínima: Vaso 10 L. Colheita: 180 dias. Adubo: Composto. Rega: 2x semana. Dica: Cresce lento.";
            case STEVIA -> "🌿 Stevia: Área mínima: Vaso 3 L. Colheita: 90 dias. Adubo: Composto. Rega: 3x semana. Dica: Boa luz.";
            case MELISSA -> "🌿 Melissa: Área mínima: Vaso 3 L. Colheita: 60 dias. Adubo: Composto. Rega: 3x semana. Dica: Aromática.";
            case SALVIA -> "🌿 Sálvia: Área mínima: Vaso 3 L. Colheita: 60 dias. Adubo: Composto. Rega: 2x semana. Dica: Solo drenado.";
            case LAVANDA -> "🪻 Lavanda: Área mínima: Vaso 5 L. Colheita: 120 dias. Adubo: Composto leve. Rega: 2x semana. Dica: Muito sol.";
            case MARACUJA -> "🍈 Maracujá: Área mínima: 2 m². Colheita: 120 dias. Adubo: Composto. Rega: 3x semana. Dica: Precisa suporte.";
            case MELANCIA_MINI -> "🍉 Melancia mini: Área mínima: 2 m². Colheita: 90 dias. Adubo: Composto. Rega: 3x semana. Dica: Muito sol.";
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
        boolean isHerb = species == Species.CEBOLINHA || species == Species.SALSINHA || species == Species.MANJERICAO || species == Species.HORTELA
                || species == Species.COENTRO || species == Species.FUNCHO || species == Species.TOMILHO || species == Species.ALECRIM
                || species == Species.OREGANO || species == Species.CAPIM_LIMAO || species == Species.LOURO || species == Species.STEVIA
                || species == Species.MELISSA || species == Species.SALVIA || species == Species.LAVANDA;
        
        boolean isVegetable = species == Species.TOMATE || species == Species.ALFACE || species == Species.MORANGO || species == Species.PIMENTA
                || species == Species.RUCULA || species == Species.ESPINAFRE || species == Species.COUVE || species == Species.TOMATE_CEREJA
                || species == Species.PIMENTAO || species == Species.PEPINO || species == Species.ABOBRINHA || species == Species.BERINJELA
                || species == Species.CENOURA || species == Species.BETERRABA || species == Species.RABANETE || species == Species.ALHO
                || species == Species.CEBOLA || species == Species.ERVILHA || species == Species.FEIJAO || species == Species.FEIJAO_VAGEM
                || species == Species.MILHO_ANAO || species == Species.BATATA || species == Species.BATATA_DOCE || species == Species.ALMEIRAO
                || species == Species.ACELGA || species == Species.MOSTARDA || species == Species.ENDIVIA || species == Species.AGRIAO
                || species == Species.CHICORIA || species == Species.NABO || species == Species.QUIABO || species == Species.AMENDOIM
                || species == Species.GENGIBRE || species == Species.CURCUMA || species == Species.MARACUJA || species == Species.MELANCIA_MINI;

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
