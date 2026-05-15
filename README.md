# Grevia API

Plataforma de gerenciamento inteligente de plantas com recomendações de cuidados e gamificação.

**Stack**: Spring Boot 3.5 · Java 21 · MySQL · JWT

---

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

---

## 🚀 Como começar

### Base URL
```
http://localhost:8080
```
**Swagger UI**: `http://localhost:8080/swagger-ui.html`

### Autenticação
JWT Bearer Token. Expira em **24 horas**.
```
Authorization: Bearer <JWT_TOKEN>
```

Rotas públicas:
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/forgot-password`
- `POST /api/auth/reset-password`

### CORS
Origens permitidas: `localhost:5173`, `localhost:3000`, `https://grevia-app.vercel.app`

### Rate Limiting
| Rota | Limite |
|------|--------|
| `/api/auth/**` | 10 req / 15 min por IP |
| Todas as outras | 60 req/min · 500 req/hora por IP |

Resposta ao exceder: **HTTP 429**

---

## 📚 Documentação

| Documento | Descrição |
|---|---|
| 📖 [Arquitetura do Backend](docs/backend-architecture.md) | Módulos, segurança, filtros e padrões |
| 🚀 [Guia de Execução Local](docs/how-to-run.md) | Como rodar DB + API no seu computador |
| 📡 [Referência de Endpoints](docs/api-endpoints.md) | Todos os endpoints da API documentados |
| ☁️ [Guia de Deploy](README-deploy.md) | Deploy com Docker em VM (Oracle Cloud) |

---

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

## Endpoints Principais

### Auth

#### Registro
`POST /api/auth/register`
Resposta 200:
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "role": "USER",
  "status": "Active",
  "lastCareDate": null,
  "currentStreak": 0,
  "totalCareActions": 0,
  "totalPoints": 0,
  "gardenerLevel": "Jardineiro Iniciante",
  "gardenerLevelNumber": 1
}
```

---

### Usuário

#### Perfil do usuário logado
`GET /api/users/me`
Resposta 200:
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "role": "USER",
  "status": "Active",
  "lastCareDate": "2026-05-13",
  "currentStreak": 5,
  "totalCareActions": 42,
  "totalPoints": 210,
  "gardenerLevel": "Jardineiro Aprendiz",
  "gardenerLevelNumber": 2
}
```

---

### Plantas

#### Criar planta
`POST /api/plants`
Body:
```json
{
  "name": "Minha Samambaia",
  "species": "SAMAMBAIA",
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
  "recommendations": "...",
  "soilType": "HUMOSO",
  "ownerName": "João Silva",
  "utility": "ORNAMENTAL",
  "utilityDisplayName": "Ornamental",
  "soilTypeDisplayName": "Humoso",
  "progressPercentage": 1.4,
  "daysRemaining": 69,
  "status": "ALIVE"
}
```

---

## Gamificação e Níveis

### Sistema de Pontos
Pontos ganhos ao concluir um cuidado:

| Condição | Pontos |
|----------|--------|
| Base | +10 |
| Cuidado no dia exato | +15 |
| Cuidado 1-2 dias fora | +5 |
| Multiplicador de streak | `currentStreak × 2` |

**Streak**: incrementa se o cuidado ocorrer no dia seguinte consecutivo. Zera se pular um dia.

### Níveis de Jardineiro
O título do usuário evolui conforme os pontos acumulados:

| Nível | Título | Emoji | Pontos Mínimos |
|-------|--------|-------|----------------|
| 1 | Jardineiro Iniciante | 🌱 | 0 |
| 2 | Jardineiro Aprendiz | 🌿 | 50 |
| 3 | Jardineiro Dedicado | 🪴 | 200 |
| 4 | Mestre Botânico | 🌳 | 500 |

---

## Fluxo Típico para o Frontend

```
1. POST /api/auth/register  → cria conta
2. POST /api/auth/login     → obtém JWT
3. GET /api/users/me        → perfil + nível
4. POST /api/plants         → cadastrar planta
5. POST /api/plants/{plantId}/cares/{careId}/complete → ganhar pontos
6. GET /api/users/me        → ver evolução de nível
7. Em qualquer 401 → limpar token e redirecionar para login
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
