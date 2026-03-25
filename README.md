# 🌱 Grevia API

> Backend inteligente de recomendação e cuidados de plantas — ajudando universitários a trazer mais verde para o dia a dia.

[![Java 21](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://adoptium.net/)
[![Spring Boot 3.5](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![MySQL 8](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)

---

## 🎯 Propósito

A Grevia descomplicar o cultivo de plantas, oferecendo **recomendações inteligentes** e **planos de cuidados personalizados**. O motor analisa o tipo de terreno e de planta (incluindo utilitárias) para sugerir as melhores espécies, enquanto os planos de cuidado gerenciam frequências de rega, registros de manutenção e muito mais.

## ✨ Funcionalidades Principais

| Funcionalidade | Descrição |
|---|---|
| 🔐 **Autenticação JWT** | Registro, login, recuperação de senha com token por e-mail |
| 🌿 **CRUD de Plantas** | Criação, edição, exclusão e listagem de plantas do usuário |
| 📸 **Upload de Imagens** | Upload via Cloudinary para fotos das plantas |
| 🧠 **Recomendações** | Motor inteligente que sugere espécies baseado em terreno e tipo |
| 📋 **Planos de Cuidado** | Cronogramas personalizados com frequências de rega dinâmicas |
| ✅ **Registros de Cuidado** | Histórico de cuidados realizados por planta |
| 💡 **Sugestões de Espécies** | Usuários podem sugerir novas espécies para o catálogo |
| 📰 **Feed Comunitário** | Visualização pública de plantas cadastradas |
| 🛡️ **Rate Limiting** | Proteção contra abuso via Bucket4j |
| 👑 **Administração** | Promoção de usuários a Admin |

## 🛠️ Stack Tecnológica

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.5.11 (Web, Security, Data JPA, Mail, Validation, Actuator)
- **Banco de Dados:** MySQL 8.0
- **Segurança:** Spring Security + JWT (JJWT 0.12.3)
- **Mapeamento:** MapStruct 1.5.5 + Lombok
- **Upload de Imagens:** Cloudinary SDK 1.36.0
- **E-mail:** Spring Mail + Resend SDK 3.1.0
- **Rate Limiting:** Bucket4j 8.10.1
- **Documentação:** Springdoc OpenAPI 2.8.15 (Swagger UI)
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
│   ├── security/            #   Rate Limiting filter
│   ├── service/             #   Serviços transversais (Cloudinary, Email)
│   └── storage/             #   Abstração de armazenamento
├── plant/                   # Domínio de Plantas
│   ├── controller/          #   PlantRestController, SpeciesSuggestionRestController
│   ├── dto/                 #   Request/Response DTOs
│   ├── enums/               #   Species, PlantType, TerrainType
│   ├── mapper/              #   MapStruct mappers
│   ├── model/               #   Entidades JPA
│   ├── repository/          #   Spring Data repositories
│   └── service/             #   PlantService, RecommendationService, SuggestionService
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

*Cultivando o conhecimento.* 🌱
