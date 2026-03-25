# Arquitetura e Documentação Técnica — Grevia API

Visão geral das decisões arquiteturais, tecnologias, módulos e padrões de segurança do backend.

---

## 🚀 Stack Tecnológica

| Categoria | Tecnologia | Versão |
|---|---|---|
| Linguagem | Java (OpenJDK Temurin) | 21 |
| Framework | Spring Boot | 3.5.11 |
| Persistência | Spring Data JPA + Hibernate | — |
| Banco de Dados | MySQL | 8.0 |
| Segurança | Spring Security + JJWT | 0.12.3 |
| Mapeamento (DTOs) | MapStruct + Lombok | 1.5.5 |
| Rate Limiting | Bucket4j | 8.10.1 |
| Documentação da API | Springdoc OpenAPI (Swagger UI) | 2.8.15 |
| Upload de Imagens | Cloudinary Java SDK | 1.36.0 |
| E-mail Transacional | Spring Mail + Resend Java SDK | 3.1.0 |
| Observabilidade | Spring Actuator | — |
| Containerização | Docker (Multi-Stage Build) | — |

---

## 🏗️ Módulos do Sistema

### 1. Core — Infraestrutura Compartilhada (`core/`)

Tudo que é **transversal** ao domínio fica aqui:

| Pacote | Responsabilidade | Principais Classes |
|---|---|---|
| `core.auth` | Autenticação e autorização | `AuthRestController`, `JwtService`, DTOs de login/registro/forgot-password |
| `core.config` | Configs globais do Spring | `SecurityConfig`, `CloudinaryConfig`, `SpringDocConfig`, `JwtAuthenticationFilter`, `ActionLoggingFilter` |
| `core.security` | Proteção contra abuso | `RateLimitingFilter` (Bucket4j) |
| `core.service` | Serviços de infraestrutura | `CloudinaryService` (upload de imagens), `EmailService` (envio de e-mails) |

### 2. Plant — Domínio de Plantas (`plant/`)

Gerencia o **catálogo de plantas**, **recomendações inteligentes** e **sugestões da comunidade**.

| Componente | Descrição |
|---|---|
| `PlantRestController` | CRUD completo de plantas + feed comunitário + upload de imagens |
| `SpeciesSuggestionRestController` | Endpoint para submissão e listagem de sugestões de novas espécies |
| `PlantService` | Lógica de negócio de plantas (criação, atualização, deleção, validações de posse) |
| `PlantRecommendationService` | Motor de recomendação baseado em tipo de terreno + tipo de planta (50+ espécies catalogadas) |
| `SpeciesSuggestionService` | Gestão de sugestões de espécies feitas pela comunidade |

### 3. Care — Domínio de Cuidados (`care/`)

Gerencia **planos de cuidado** e o **histórico de registros de manutenção** das plantas.

| Componente | Descrição |
|---|---|
| `CarePlanRestController` | CRUD de planos de cuidado vinculados a uma planta |
| `CareRecordRestController` | Criação e listagem de registros de cuidados realizados |
| `CarePlanService` | Lógica de criação e atualização de planos de cuidado |
| `CareRecordService` | Lógica de registros de cuidados (rega, poda, etc.) |
| `SpeciesCareService` | Definição de métricas padrão por espécie (frequência de rega, cuidados default) |

### 4. User — Domínio de Usuários (`user/`)

Gerencia o **perfil**, **desativação de conta** e **promoção de roles**.

| Componente | Descrição |
|---|---|
| `UserRestController` | Perfil do usuário (`/me`), atualização, desativação e promoção a Admin |
| `UserService` | Lógica de negócio de usuários, incluindo criação, recuperação de senha e promoção |

---

## 📊 Diagrama de Classes (Entidades JPA)

```mermaid
classDiagram
    direction LR

    class User {
        Long id
        String name
        String email
        String password
        Role role
        Status status
        LocalDate lastCareDate
        Integer currentStreak
        Integer totalCareActions
        Integer totalPoints
        String resetPasswordToken
        LocalDateTime resetPasswordTokenExpiry
    }

    class Plant {
        Long id
        String name
        Species species
        String customSpeciesName
        String recommendations
        SoilType soilType
        String imagePath
    }

    class SpeciesSuggestion {
        Long id
        String suggestedName
        String description
        LocalDateTime submittedAt
    }

    class CarePlan {
        Long id
        CareType careType
        FrequencyType frequencyType
        LocalDate nextCareDate
    }

    class CareRecord {
        Long id
        String notes
        LocalDate careDate
    }

    class Role {
        <<enumeration>>
        ADMIN
        USER
    }

    class Status {
        <<enumeration>>
        Active
        Inactive
    }

    class Species {
        <<enumeration>>
        ESPADA_DE_SAO_JORGE
        SAMAMBAIA
        SUCULENTA
        CACTO
        ORQUIDEA
        ALFACE
        MANJERICAO
        TOMATE
        CENOURA
        OUTRA
        ... 50+ espécies
    }

    class PlantUtility {
        <<enumeration>>
        ORNAMENTAL
        HORTALICA_SALADA
        TEMPERO_ERVA
        FRUTA
        LEGUME_RAIZ
        OUTREM
    }

    class SoilType {
        <<enumeration>>
        ARENOSO
        ARGILOSO
        HUMOSO
        CALCARIO
        MISTO
    }

    class CareType {
        <<enumeration>>
        REGA
        PODA
        ADUBACAO
        TRANSPLANTE
        CONTROLE_PRAGAS
        OUTRO
    }

    class FrequencyType {
        <<enumeration>>
        DIARIO
        SEMANAL
        DUAS_VEZES_SEMANA
        TRES_VEZES_SEMANA
        QUINZENAL
        MENSAL
        BIMESTRAL
        SOB_DEMANDA
    }

    User "1" --> "*" Plant : possui
    User "1" --> "*" SpeciesSuggestion : submete
    Plant "1" --> "*" CarePlan : tem
    CarePlan "1" --> "*" CareRecord : registra

    User --> Role
    User --> Status
    Plant --> Species
    Plant --> SoilType
    Species --> PlantUtility
    CarePlan --> CareType
    CarePlan --> FrequencyType
```

---

## 🔒 Segurança


### Fluxo de Autenticação (JWT)

```mermaid
sequenceDiagram
    participant C as 🖥️ Cliente
    participant A as AuthController
    participant J as JwtService
    participant DB as 🗄️ Banco de Dados

    Note over C,DB: 1. Login
    C->>A: POST /api/auth/login {email, senha}
    A->>DB: Verifica credenciais (BCrypt)
    DB-->>A: ✅ Usuário válido
    A->>J: generateToken(userDetails)
    J-->>A: Token JWT (eyJ...)
    A-->>C: 200 OK { token: "eyJ..." }

    Note over C,DB: 2. Requisição Autenticada
    C->>A: GET /api/plants (Header: Bearer eyJ...)
    A->>J: validateToken(token)
    J-->>A: ✅ Token válido → UserDetails
    A->>DB: Busca dados do usuário
    DB-->>A: Dados
    A-->>C: 200 OK { plantas }
```

1. O usuário se autentica via e-mail + senha (senha armazenada com hashing seguro via BCrypt).
2. Um token JWT é gerado com Claims contendo o ID do usuário.
3. Rotas protegidas validam o header `Authorization: Bearer <token>` a cada requisição.

### Fluxo de Recuperação de Senha

```mermaid
flowchart LR
    A["👤 Usuário esqueceu a senha"] --> B["POST /forgot-password\n{email}"]
    B --> C{"Usuário existe?"}
    C -- Sim --> D["Gera token temporário"]
    D --> E["📧 Envia e-mail com token"]
    E --> F["Usuário recebe o e-mail"]
    F --> G["POST /reset-password\n{token, novaSenha}"]
    G --> H{"Token válido?"}
    H -- Sim --> I["✅ Senha atualizada (BCrypt)"]
    H -- Não --> J["❌ 400 Token inválido"]
    C -- Não --> K["200 OK (sem revelar)"]
```

### Ciclo de Vida de uma Requisição HTTP (Filtros)

```mermaid
flowchart TD
    A["🌐 Requisição HTTP"] --> B["RateLimitingFilter"]
    B -->|"Limite OK"| C["JwtAuthenticationFilter"]
    B -->|"Limite excedido"| X["❌ 429 Too Many Requests"]
    C -->|"Token válido"| D["ActionLoggingFilter"]
    C -->|"Token inválido"| Y["❌ 401 Unauthorized"]
    C -->|"Rota pública"| D
    D --> E["SecurityFilterChain"]
    E --> F["🎯 Controller"]
    F --> G["Service → Repository"]
    G --> H["🗄️ MySQL"]
    H --> I["✅ Response ao Cliente"]
```

### Rate Limiting (Bucket4j)

O `RateLimitingFilter` aplica o algoritmo de Token Bucket para:
- Proteger contra ataques DDoS.
- Limitar tentativas de força bruta em rotas de login/registro.
- Garantir uso sustentável em ambientes compartilhados.

---

## 📸 Upload de Imagens (Cloudinary)

O serviço `CloudinaryService` gerencia o upload de imagens para o Cloudinary:
- Endpoint: `POST /api/plants/{id}/image` (multipart/form-data)
- Limite de arquivo: 5 MB
- A URL da imagem é salva na entidade Plant e retornada no DTO de resposta.

---

## 📧 Serviço de E-mail

O `EmailService` utiliza o **Resend SDK** para envio de e-mails transacionais:
- Recuperação de senha (envio do token de reset)
- Remetente: `contactgrevia@gmail.com`

---

## 📈 Observabilidade (Actuator)

Endpoints expostos:
- `GET /actuator/health` — Status da aplicação (com detalhes)
- `GET /actuator/info` — Informações da aplicação
- `GET /actuator/metrics` — Métricas de performance

---

## 🐳 Infraestrutura Docker

| Arquivo | Uso |
|---|---|
| `Dockerfile` | Build multi-stage (Maven build → JRE Alpine runtime) |
| `docker-compose.yml` | Ambiente de desenvolvimento (MySQL + API) |
| `docker-compose.prod.yml` | Ambiente de produção (com variáveis via `.env`) |

O Dockerfile usa **multi-stage build** para criar uma imagem final enxuta (~150MB) sem o Maven instalado.
