# 📡 Referência Completa de Endpoints — Grevia API

> Base URL: `http://localhost:8080` (dev) | `https://grevia-api-production.up.railway.app` (prod)

Todos os endpoints protegidos (✅) requerem o envio do header:
```http
Authorization: Bearer <seu-token-jwt>
```

---

## 🔐 Autenticação (`/api/auth`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/auth/register` | ❌ | Cadastro de novo usuário |
| `POST` | `/api/auth/login` | ❌ | Login (retorna token JWT) |
| `POST` | `/api/auth/forgot-password` | ❌ | Solicita recuperação de senha (envia e-mail) |
| `POST` | `/api/auth/reset-password` | ❌ | Redefine senha com token recebido por e-mail |

### Payload de Exemplos

**Registro:**
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senhaSegura123"
}
```

**Login:**
```json
{
  "email": "joao@email.com",
  "password": "senhaSegura123"
}
// Resposta: { "token": "eyJhbG..." }
```

---

## 👤 Usuários (`/api/users`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `GET` | `/api/users/me` | ✅ | Retorna o perfil do usuário autenticado |
| `PUT` | `/api/users/me` | ✅ | Atualiza o perfil do usuário autenticado |
| `PUT` | `/api/users/me/password` | ✅ | Altera a senha do usuário autenticado |
| `DELETE` | `/api/users/me` | ✅ | Exclui permanentemente a conta do usuário (Hard Delete) |
| `PATCH` | `/api/users/{id}/promote` | ✅ 👑 | Promove usuário a ADMIN (somente admins) |

### Resposta do Perfil (`GET /api/users/me`)
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
  "gardenerLevel": "🪴 Jardineiro Dedicado",
  "gardenerLevelEmoji": "🪴",
  "gardenerLevelNumber": 3
}
```

---

## 🌿 Plantas (`/api/plants`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/plants` | ✅ | Cria uma nova planta |
| `GET` | `/api/plants` | ✅ | Lista todas as plantas ativas do usuário |
| `GET` | `/api/plants/{id}` | ✅ | Retorna detalhes de uma planta específica |
| `PUT` | `/api/plants/{id}` | ✅ | Atualiza informações de uma planta |
| `DELETE` | `/api/plants/{id}` | ✅ | Remove uma planta completamente |
| `PATCH` | `/api/plants/{id}/harvest` | ✅ | Realiza a colheita de uma planta |
| `PATCH` | `/api/plants/{id}/archive` | ✅ | Arquiva uma planta (Body: notas/motivo em string) |
| `GET` | `/api/plants/history` | ✅ | Histórico paginado (Query: `page`, `size`) |
| `GET` | `/api/plants/feed` | ✅ | Feed comunitário (todas as plantas) |
| `GET` | `/api/plants/species` | ✅ | Lista de espécies disponíveis e utilidade |

### Payload de Exemplos

**Criação de Planta (`POST /api/plants`):**
```json
{
  "name": "Minha Samambaia",
  "species": "SAMAMBAIA",
  "soilType": "ARGILOSO"
}
```

**Resposta de Planta (`GET /api/plants/{id}`):**
```json
{
  "id": 1,
  "name": "Minha Samambaia",
  "species": "SAMAMBAIA",
  "customSpeciesName": null,
  "recommendations": "...",
  "soilType": "ARGILOSO",
  "ownerName": "João Silva",
  "utility": "ORNAMENTAL",
  "utilityDisplayName": "Ornamental",
  "soilTypeDisplayName": "Argiloso",
  "progressPercentage": 15.5,
  "daysRemaining": 45,
  "status": "ALIVE"
}
```

**Arquivar Planta (`PATCH /api/plants/{id}/archive`):**
```json
"A planta secou durante o inverno."
```

**Lista de Espécies (`GET /api/plants/species`):**
```json
[
  {
    "name": "ESPADA_DE_SAO_JORGE",
    "formattedName": "Espada de Sao Jorge",
    "utility": "ORNAMENTAL"
  }
]
```

---

## 💡 Feedback do App (`/api/feedback`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/feedback` | ✅ | Envia um feedback sobre o aplicativo |
| `GET` | `/api/feedback` | ✅ | Lista todos os feedbacks recebidos |

**Exemplo de Payload (`POST`):**
```json
{
  "category": "SUGESTÃO",
  "message": "Seria legal ter um modo noturno no app."
}
```

---

## 📋 Planos de Cuidado (`/api/plants/{plantId}/cares`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/plants/{plantId}/cares` | ✅ | Cria um plano de cuidado para a planta |
| `GET` | `/api/plants/{plantId}/cares` | ✅ | Lista os planos de cuidado da planta |
| `PUT` | `/api/plants/{plantId}/cares/{careId}` | ✅ | Atualiza um plano de cuidado |
| `DELETE` | `/api/plants/{plantId}/cares/{careId}` | ✅ | Remove um plano de cuidado |
| `POST` | `/api/plants/{plantId}/cares/{careId}/complete`| ✅ | Conclui um cuidado (Body: notas em string) |

### Payload de Exemplos

**Concluir cuidado (`POST .../complete`):**
```json
"Reguei com 200ml de água."
```
> **Nota:** Marcar como concluído atualiza automaticamente a data do próximo cuidado, registra no histórico e concede pontos ao usuário.

---

## ✅ Registros de Cuidado (`/api/cares/{carePlanId}/records`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/cares/{carePlanId}/records` | ✅ | Adiciona um registro manual de cuidado |
| `GET` | `/api/cares/{carePlanId}/records` | ✅ | Lista os registros históricos de um plano |

---

## 📈 Observabilidade (`/actuator`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `GET` | `/actuator/health` | ❌ | Status de saúde da aplicação |
| `GET` | `/actuator/info` | ❌ | Informações da aplicação |
| `GET` | `/actuator/metrics` | ❌ | Métricas de performance |

---

## 📖 Documentação Interativa (Swagger)

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/api-docs`
