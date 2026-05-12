# Grevia API

Plataforma de gerenciamento inteligente de plantas com recomendações de cuidados e gamificação.

**Stack**: Spring Boot 3.5 · Java 21 · MySQL · JWT

---

<<<<<<< HEAD
## Base URL

```
http://localhost:8080
```

**Swagger UI**: `http://localhost:8080/swagger-ui.html`

---

## Autenticação

JWT Bearer Token. Expira em **24 horas**.

```
Authorization: Bearer <JWT_TOKEN>
```

Rotas públicas (sem token):
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/forgot-password`
- `POST /api/auth/reset-password`

---

## CORS

Origens permitidas: `localhost:5173`, `localhost:3000`, `https://grevia-app.vercel.app`

---

## Rate Limiting

| Rota | Limite |
|------|--------|
| `/api/auth/**` | 10 req / 15 min por IP |
| Todas as outras | 60 req/min · 500 req/hora por IP |

Resposta ao exceder: **HTTP 429**
```json
{ "erro": "Muitas requisições em pouco tempo. Por favor, aguarde um momento e tente novamente." }
=======
## 🎯 Propósito

A Grevia descomplicar o cultivo de plantas, oferecendo **recomendações inteligentes** e **planos de cuidados personalizados**. O motor analisa o tipo de terreno e de planta (incluindo utilitárias) para sugerir as melhores espécies, enquanto os planos de cuidado gerenciam frequências de rega, registros de manutenção e muito mais.

## ✨ Funcionalidades Principais

| Funcionalidade | Descrição |
|---|---|
| 🔐 **Autenticação JWT** | Registro, login, recuperação de senha com token por e-mail |
| 🌿 **CRUD de Plantas** | Criação, edição, exclusão e listagem de plantas do usuário |
| 🧠 **Recomendações** | Motor inteligente que sugere espécies baseado em terreno e tipo |
| 📋 **Planos de Cuidado** | Cronogramas personalizados com frequências de rega dinâmicas |
| ✅ **Registros de Cuidado** | Histórico de cuidados realizados por planta |
| 💡 **Feedback do App** | Usuários podem enviar feedbacks gerais e sugestões de melhoria |
| 📰 **Feed Comunitário** | Visualização pública de plantas cadastradas |
| 🛡️ **Rate Limiting** | Proteção contra abuso via Bucket4j |
| 👑 **Administração** | Promoção de usuários a Admin |

## 🛠️ Stack Tecnológica

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.5.11 (Web, Security, Data JPA, Mail, Validation, Actuator)
- **Banco de Dados:** MySQL 8.0
- **Segurança:** Spring Security + JWT (JJWT 0.12.3)
- **Mapeamento:** MapStruct 1.5.5 + Lombok
- **E-mail:** Spring Mail + Resend SDK 3.1.0
- **Rate Limiting:** Bucket4j 8.10.1
- **Documentação:** Springdoc OpenAPI 2.8.15 (Swagger UI)
- **Testes:** JUnit 5, Mockito, MockMvc
- **Infraestrutura:** Docker + Docker Compose
- **Observabilidade:** Spring Actuator (health, info, metrics)

## 📚 Documentação

| Documento | Descrição |
|---|---|
| 📖 [Arquitetura do Backend](docs/backend-architecture.md) | Módulos, segurança, filtros e padrões |
| 🚀 [Guia de Execução Local](docs/how-to-run.md) | Como rodar DB + API no seu computador |
| 📡 [Referência de Endpoints](docs/api-endpoints.md) | Todos os endpoints da API documentados |
| ☁️ [Guia de Deploy](README-deploy.md) | Deploy com Docker em VM (Oracle Cloud) |

## 🏗️ Estrutura do Projeto

```
src/main/java/com/projeto1cc/grevia/
├── core/                    # Infraestrutura compartilhada
│   ├── auth/                #   Autenticação (controller, DTOs, JWT service)
│   ├── config/              #   Configurações (Security, Cloudinary, SpringDoc, Filtros)
│   ├── feedback/            #   Gestão de Feedback do App
│   ├── security/            #   Rate Limiting filter
│   ├── service/             #   Serviços transversais (Cloudinary, Email)
│   └── storage/             #   Abstração de armazenamento
├── plant/                   # Domínio de Plantas
│   ├── controller/          #   PlantRestController
│   ├── dto/                 #   Request/Response DTOs
│   ├── enums/               #   Species, PlantType, TerrainType
│   ├── mapper/              #   MapStruct mappers
│   ├── model/               #   Entidades JPA
│   ├── repository/          #   Spring Data repositories
│   └── service/             #   PlantService, RecommendationService
├── care/                    # Domínio de Cuidados
│   ├── controller/          #   CarePlanRestController, CareRecordRestController
│   ├── dto/                 #   Request/Response DTOs
│   ├── enums/               #   CareType, WateringFrequency
│   ├── mapper/              #   MapStruct mappers
│   ├── model/               #   Entidades JPA
│   ├── repository/          #   Spring Data repositories
│   └── service/             #   CarePlanService, CareRecordService, SpeciesCareService
└── user/                    # Domínio de Usuários
    ├── controller/          #   UserRestController
    ├── dto/                 #   Request/Response DTOs
    ├── mapper/              #   MapStruct mappers
    ├── model/               #   Entidade User
    ├── repository/          #   UserRepository
    └── service/             #   UserService
>>>>>>> 2c93c95d113d71980f231decae191d1a1fdfd121
```

---

## Erros de Autenticação

**Token expirado** → HTTP 401
```json
{ "error": "Token expirado", "code": "TOKEN_EXPIRED" }
```

**Token inválido** → HTTP 401
```json
{ "error": "Token inválido", "code": "TOKEN_INVALID" }
```

---

## Endpoints

### Auth

#### Registro
```
POST /api/auth/register
```
Body:
```json
{
  "name": "string",
  "email": "user@example.com",
  "password": "string"
}
```
Resposta 200:
```json
{
  "name": "string",
  "email": "user@example.com",
  "role": "USER",
  "status": "Active",
  "lastCareDate": "2026-05-04",
  "currentStreak": 0,
  "totalCareActions": 0,
  "totalPoints": 0
}
```

---

#### Login
```
POST /api/auth/login
```
Body:
```json
{
  "email": "user@example.com",
  "password": "string"
}
```
Resposta 200:
```json
{
  "token": "eyJhbGci..."
}
```

---

#### Solicitar redefinição de senha
```
POST /api/auth/forgot-password
```
Body:
```json
{
  "email": "user@example.com"
}
```
Resposta 200: vazio

---

#### Redefinir senha
```
POST /api/auth/reset-password
```
Body:
```json
{
  "token": "uuid-do-email",
  "newPassword": "nova-senha"
}
```
Validação: `newPassword` mínimo 6 caracteres.

Resposta 200: vazio

---

### Usuário

#### Perfil do usuário logado
```
GET /api/users/me
Authorization: Bearer <token>
```
Resposta 200:
```json
{
  "name": "string",
  "email": "user@example.com",
  "role": "USER",
  "status": "Active",
  "lastCareDate": "2026-05-04",
  "currentStreak": 5,
  "totalCareActions": 42,
  "totalPoints": 870
}
```

---

#### Atualizar perfil
```
PUT /api/users/me
Authorization: Bearer <token>
```
Body:
```json
{
  "name": "novo nome",
  "email": "novo@email.com",
  "password": "nova-senha"
}
```
Resposta 200: mesmo formato de `GET /api/users/me`

---

#### Alterar senha
```
PUT /api/users/me/password
Authorization: Bearer <token>
```
Body:
```json
{
  "currentPassword": "senha-atual",
  "newPassword": "nova-senha"
}
```
Resposta 200: vazio

---

#### Excluir conta
```
DELETE /api/users/me
Authorization: Bearer <token>
```
Resposta 204: sem corpo

---

#### Promover usuário a Admin
```
PATCH /api/users/{id}/promote
Authorization: Bearer <token> (apenas ADMIN)
```
Resposta 200: vazio

---

### Plantas

#### Criar planta
```
POST /api/plants
Authorization: Bearer <token>
```
Body:
```json
{
  "name": "Minha Samambaia",
  "species": "SAMAMBAIA",
  "customSpeciesName": "opcional",
  "soilType": "HUMOSO"
}
```
Resposta 200:
```json
{
  "id": 1,
  "name": "Minha Samambaia",
  "species": "SAMAMBAIA",
  "customSpeciesName": null,
  "recommendations": "texto de recomendações gerado automaticamente",
  "soilType": "HUMOSO",
  "ownerName": "Nome do Usuário",
  "utility": "ORNAMENTAL"
}
```
> Ao criar uma planta, planos de cuidado são gerados automaticamente com base na espécie.

---

#### Listar plantas do usuário
```
GET /api/plants
Authorization: Bearer <token>
```
Resposta 200: array de `PlantResponseDTO`

---

#### Buscar planta por ID
```
GET /api/plants/{id}
Authorization: Bearer <token>
```
Resposta 200: `PlantResponseDTO`

---

#### Atualizar planta
```
PUT /api/plants/{id}
Authorization: Bearer <token>
```
Body: mesmo formato de criação

Resposta 200: `PlantResponseDTO`

---

#### Deletar planta
```
DELETE /api/plants/{id}
Authorization: Bearer <token>
```
Resposta 204: sem corpo

---

#### Feed público de plantas
```
GET /api/plants/feed
Authorization: Bearer <token>
```
Resposta 200: array com plantas de todos os usuários

---

### Planos de Cuidado

#### Criar plano de cuidado
```
POST /api/plants/{plantId}/cares
Authorization: Bearer <token>
```
Body:
```json
{
  "careType": "REGA",
  "frequencyType": "SEMANAL",
  "startDate": "2026-05-04"
}
```
`startDate` é opcional (padrão: hoje).

Resposta 200:
```json
{
  "id": 1,
  "careType": "REGA",
  "frequencyType": "SEMANAL",
  "nextCareDate": "2026-05-11",
  "lastCareDate": null,
  "plantId": 1
}
```

---

#### Listar planos de cuidado da planta
```
GET /api/plants/{plantId}/cares
Authorization: Bearer <token>
```
Resposta 200: array de `CarePlanResponseDTO`

---

#### Atualizar plano de cuidado
```
PUT /api/plants/{plantId}/cares/{careId}
Authorization: Bearer <token>
```
Body: mesmo formato de criação

Resposta 200: `CarePlanResponseDTO`

---

#### Deletar plano de cuidado
```
DELETE /api/plants/{plantId}/cares/{careId}
Authorization: Bearer <token>
```
Resposta 204: sem corpo

---

#### Concluir cuidado (marcar como feito)
```
POST /api/plants/{plantId}/cares/{careId}/complete
Authorization: Bearer <token>
```
Body:
```json
"Usei 500ml de água."
```
Resposta 200: `CarePlanResponseDTO` com `nextCareDate` e `lastCareDate` atualizados

Efeitos colaterais:
- `nextCareDate` recalculado automaticamente
- `lastCareDate` = hoje
- Cria um `CareRecord`
- Atualiza métricas do usuário (pontos, streak, totalCareActions)

> Após chamar este endpoint, fazer `GET /api/users/me` para buscar métricas atualizadas.

---

### Registros de Cuidado

#### Criar registro de cuidado
```
POST /api/cares/{carePlanId}/records
Authorization: Bearer <token>
```
Body:
```json
{
  "notes": "opcional",
  "careDate": "2026-05-04"
}
```
`careDate` é opcional (padrão: hoje).

Resposta 200:
```json
{
  "id": 1,
  "notes": "opcional",
  "careDate": "2026-05-04",
  "carePlanId": 1
}
```
Efeitos colaterais: mesmo que `/complete`

---

#### Listar registros de um plano
```
GET /api/cares/{carePlanId}/records
Authorization: Bearer <token>
```
Resposta 200: array de `CareRecordResponseDTO`

---

### Feedback do App

#### Enviar feedback
```
POST /api/feedback
Authorization: Bearer <token>
```
Body:
```json
{
  "category": "SUGESTÃO",
  "message": "descrição do feedback"
}
```
Resposta 200:
```json
{
  "id": 1,
  "category": "SUGESTÃO",
  "message": "descrição do feedback",
  "submittedAt": "2026-05-04T12:34:56"
}
```

---

#### Listar todos os feedbacks
```
GET /api/feedback
Authorization: Bearer <token>
```
Resposta 200: array de `AppFeedbackResponseDTO`

---

## Enumerações

### CareType
| Valor | Descrição |
|-------|-----------|
| `REGA` | Rega |
| `PODA` | Poda |
| `ADUBACAO` | Adubação |
| `TRANSPLANTE` | Transplante |
| `CONTROLE_PRAGAS` | Controle de Pragas |
| `OUTRO` | Outro |

---

### FrequencyType
| Valor | Próxima data |
|-------|-------------|
| `DIARIO` | +1 dia |
| `DUAS_VEZES_SEMANA` | +3 dias |
| `TRES_VEZES_SEMANA` | +2 dias |
| `SEMANAL` | +1 semana |
| `QUINZENAL` | +2 semanas |
| `MENSAL` | +1 mês |
| `BIMESTRAL` | +2 meses |
| `SOB_DEMANDA` | sem agendamento |

---

### Species (71 espécies)

**ORNAMENTAL**: `ESPADA_DE_SAO_JORGE`, `SAMAMBAIA`, `SUCULENTA`, `CACTO`, `COSTELA_DE_ADAO`, `JIBOIA`, `ZAMIOCULCA`, `LIRIO_DA_PAZ`, `ORQUIDEA`, `ROSA`, `LAVANDA`

**HORTALICA_SALADA**: `ALFACE`, `RUCULA`, `ESPINAFRE`, `COUVE`, `ALMEIRAO`, `ACELGA`, `MOSTARDA`, `ENDIVIA`, `AGRIAO`, `CHICORIA`

**TEMPERO_ERVA**: `CEBOLINHA`, `SALSINHA`, `MANJERICAO`, `HORTELA`, `COENTRO`, `FUNCHO`, `TOMILHO`, `ALECRIM`, `OREGANO`, `CAPIM_LIMAO`, `LOURO`, `STEVIA`, `MELISSA`, `SALVIA`

**FRUTA**: `TOMATE`, `MORANGO`, `PIMENTA`, `TOMATE_CEREJA`, `PIMENTAO`, `PEPINO`, `MARACUJA`, `MELANCIA_MINI`, `ABOBRINHA`, `BERINJELA`, `QUIABO`

**LEGUME_RAIZ**: `CENOURA`, `BETERRABA`, `RABANETE`, `ALHO`, `CEBOLA`, `NABO`, `GENGIBRE`, `CURCUMA`, `BATATA`, `BATATA_DOCE`, `AMENDOIM`

**OUTREM**: `ERVILHA`, `FEIJAO`, `FEIJAO_VAGEM`, `MILHO_ANAO`, `OUTRA`

---

### SoilType
| Valor | Descrição |
|-------|-----------|
| `ARENOSO` | Arenoso |
| `ARGILOSO` | Argiloso |
| `HUMOSO` | Humoso |
| `CALCARIO` | Calcário |
| `MISTO` | Misto |

---

### PlantUtility
`ORNAMENTAL` · `HORTALICA_SALADA` · `TEMPERO_ERVA` · `FRUTA` · `LEGUME_RAIZ` · `OUTREM`

---

### Role / Status
- Role: `ADMIN` | `USER`
- Status: `Active` | `Inactive`

---

## Gamificação

Pontos ganhos ao concluir um cuidado:

| Condição | Pontos |
|----------|--------|
| Base | +10 |
| Cuidado no dia exato | +15 |
| Cuidado 1-2 dias fora | +5 |
| Multiplicador de streak | `currentStreak × 2` |

**Streak**: incrementa se o cuidado ocorrer no dia seguinte consecutivo. Zera se pular um dia.

---

## Fluxo Típico para o Frontend

```
1. POST /api/auth/register  → cria conta
2. POST /api/auth/login     → obtém JWT
3. Salvar token (localStorage / sessionStorage)
4. GET /api/users/me        → perfil + gamificação
5. POST /api/plants         → cadastrar planta (planos criados automaticamente)
6. GET /api/plants/{id}/cares → listar planos de cuidado
7. POST /api/plants/{plantId}/cares/{careId}/complete → marcar cuidado feito
8. GET /api/users/me        → atualizar pontos/streak exibidos
9. Em qualquer 401 → limpar token e redirecionar para login
```

---

## Variáveis de Ambiente

```env
MYSQLHOST=localhost
MYSQLPORT=3306
MYSQLDATABASE=greviadb
MYSQLUSER=user
MYSQLPASSWORD=admin
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=contactgrevia@gmail.com
SPRING_MAIL_PASSWORD=<app-password>
```

---

## Health Check

```
GET /actuator/health
GET /actuator/info
GET /actuator/metrics
```
