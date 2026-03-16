# Arquitetura e Documentação Técnica - Grevia API

Este documento detalha as decisões arquiteturais, tecnologias e estrutura de módulos do backend da Grevia API.

## 🚀 Stack Tecnológica
- **Linguagem:** Java 21
- **Framework Principal:** Spring Boot 3.5.11
- **Persistência de Dados:** Spring Data JPA + Hibernate
- **Banco de Dados:** MySQL
- **Segurança:** Spring Security com JWT (JSON Web Tokens) via plataforma JJWT
- **Mapeamento de Objetos (DTOs):** MapStruct + Lombok
- **Rate Limiting:** Bucket4j (Proteção contra acessos excessivos)
- **Documentação da API:** Springdoc OpenAPI (Swagger UI)

## 🏗️ Estrutura de Módulos (Domínios Principais)

### 1. Autenticação e Gestão de Usuários (`auth`/`users`)
- Endpoints de cadastro (`/api/auth/register`), Login (`/api/auth/login`) e Logout.
- Rotas de consulta e manipulação de perfil de usuário (`/api/users/me`).
- Responsabilidade na emissão de token de autenticação que servirá todo o ciclo de vida do cliente consumindo o restante da API.

### 2. Sistema de Recomendações (`PlantRecommendationService`)
- Analisa parâmetros como Tipo de Terreno (ex: Arenoso, Argiloso) e Tipos da Planta.
- Base atualizada suportando sugestões e tratamentos para diversas espécies (mais de 50 catalogadas), além do gerenciamento dinâmico que compreende interações de plantas Utilitárias.

### 3. Planos de Cuidados (`SpeciesCareService`)
- Estabelece as métricas "default" conforme a planta.
- Administra as demandas de manutenção rotineiras como o processo de agendamento e identificação de dias (lidando com diferentes frequências de rega por semana).
- Possui abstração para lidar com "Outras" (nomes de plantas customizados).

## 🔒 Segurança (Spring Security)
O controle de requisições acontece através de um *OncePerRequestFilter* em conjunto com o Spring Security e Web Security Customizers.

O fluxo de autenticação funciona assim:
1. O usuário se autentica com email e senha (hashing de forma segura).
2. Um Token JWT (JSON Web Token) é fabricado, contendo "Claims" com seu ID.
3. Todas rotas restritas avaliam e verificam o acesso no momento da autorização de request caso possuam o Header `Authorization: Bearer <seu-token-aqui>`.

## 📈 Rate Limiting (Bucket4j)
Em combinação a arquitetura, implementamos restrições usando o algoritmo de Bucket com a dependência do `Bucket4j`. Isso visa evitar ataques DDos, tentativas de Força Bruta nas rotas de registro/login e impõe um uso estável e sustentável em ambientes de recursos partilhados dentro dos serviços da aplicação.
