# Arquitetura e Documentação Técnica — Grevia API

Visão geral atualizada das decisões arquiteturais, tecnologias, módulos e padrões de segurança do backend Grevia. Esta documentação serve como o guia principal para a IA de Frontend realizar a integração.

---

## 🚀 Stack Tecnológica

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.5.11 |
| Persistência | Spring Data JPA + Hibernate |
| Banco de Dados | MySQL 8.0 |
| Segurança | Spring Security + JJWT (0.12.3) |
| Mapeamento (DTOs) | MapStruct (1.5.5) + Lombok |
| Rate Limiting | Bucket4j (8.10.1) |
| Documentação da API | Springdoc OpenAPI (Swagger UI v2.8.15) |
| E-mail | Spring Mail (JavaMailSender / Gmail SMTP) |

---

## 🗂️ Estrutura do Projeto (Domínios)

A API segue uma arquitetura de **Monólito Modular** organizada por domínios de negócio (`core`, `plant`, `care`, `user`). Cada domínio tem seu ciclo de vida completo (Controller, Service, Repository, DTO).

1. **`core/`**: Infraestrutura (Auth, JwtService, JWT Filter, RateLimiter, CORS, Email, Feedback do App).
2. **`plant/`**: Plantas do usuário (`PlantRestController`), histórico/arquivamento de plantas, colheita, feed comunitário e catálogo de espécies.
3. **`care/`**: Planos de cuidado (`CarePlanRestController`) e histórico de ações executadas (`CareRecordRestController`). Orquestra a automação da próxima data de cuidado.
4. **`user/`**: Gestão de perfil (`UserRestController`), métricas de gamificação (pontos, streak) e promoção de roles (`ADMIN`/`USER`).

---

## 🔄 Fluxo de Negócio e Gamificação

Quando o Frontend integra com a API, ele deve observar o seguinte fluxo lógico:

1. **Autenticação:** Todas as rotas (exceto `/api/auth/**` e `/actuator/**`) exigem o header `Authorization: Bearer <TOKEN>`.
2. **Gamificação:**
   - O usuário ganha pontos ao completar um cuidado (`POST /api/plants/{plantId}/cares/{careId}/complete`).
   - Se o cuidado for feito de dias consecutivos, o `currentStreak` aumenta.
   - O Frontend deve fazer um `GET /api/users/me` periodicamente (ex: após concluir um cuidado) para atualizar a HUD de gamificação na tela.
3. **Ciclo de Vida da Planta:**
   - Criação: O Spring Boot injeta planos de cuidados baseados na espécie automaticamente.
   - Colheita: `PATCH /api/plants/{id}/harvest` (apenas marcacional, sem impacto deletério).
   - Arquivamento: `PATCH /api/plants/{id}/archive` (Remove a planta da listagem ativa e move para o histórico).
   - Histórico: `GET /api/plants/history` retorna as plantas mortas/arquivadas paginadas.

---

## 🔒 Segurança

A segurança é garantida em múltiplas camadas:
- **Bucket4j:** Limite de 10 req/15min por IP nas rotas de Auth. 60 req/min no geral. (Retorna `429 Too Many Requests`).
- **JWT Stateless:** Ausência completa de estado. Os claims carregam a identidade (email) do usuário.
- **Isolamento de Recurso:** O backend extrai o `email` pelo `SecurityContextHolder` nos Services. Nenhuma rota de domínio permite que o `User A` altere dados de `User B`.
- **CORS:** Liberado via `SecurityConfig` para `http://localhost:5173`, `http://localhost:3000` e a URL de produção na Vercel.

---

## 📋 Dicionário de Enumerações e Chaves

Ao enviar requisições JSON, preste atenção aos Enums (precisam ser strings exatas):

- **`Species`:** `ESPADA_DE_SAO_JORGE`, `SAMAMBAIA`, `SUCULENTA`, `CACTO`, `TOMATE`, etc. Use `GET /api/plants/species` para puxar os valores validos.
- **`SoilType`:** `ARENOSO`, `ARGILOSO`, `HUMOSO`, `CALCARIO`, `MISTO`.
- **`CareType`:** `REGA`, `PODA`, `ADUBACAO`, `TRANSPLANTE`, `CONTROLE_PRAGAS`, `OUTRO`.
- **`FrequencyType`:** `DIARIO`, `SEMANAL`, `DUAS_VEZES_SEMANA`, `TRES_VEZES_SEMANA`, `QUINZENAL`, `MENSAL`, `BIMESTRAL`, `SOB_DEMANDA`.
- **`PlantUtility`:** `ORNAMENTAL`, `HORTALICA_SALADA`, `TEMPERO_ERVA`, `FRUTA`, `LEGUME_RAIZ`, `OUTREM`.

---

## 📝 Como a IA Frontend Deve Consumir

1. **Sempre envie o Token:** Mantenha um Interceptor (se usar Axios) ou middleware (no Fetch) injetando o Bearer. Tratamento centralizado para `401 Unauthorized` deve forçar o Logout e levar à `/login`.
2. **Atualização Otimista (Optimistic UI):** Ao marcar um plano como completo (`/complete`), a API vai recalcular a próxima data (`nextCareDate`). Atualize a interface com a resposta dessa chamada.
3. **Erros (400 Bad Request):** Falhas de validação de DTO no Backend retornam JSON padrão do Spring detalhando quais campos falharam (Ex: `email must be well-formed`).

*Para a referência exata das rotas HTTP, consulte o arquivo `docs/api-endpoints.md` ou `ARCHITECTURE.md`.*