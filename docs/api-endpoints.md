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
<<<<<<< HEAD
| `DELETE` | `/api/users/me` | ✅ | Exclui permanentemente a conta do usuário autenticado |
=======
| `DELETE` | `/api/users/me` | ✅ | Exclui a conta do usuário permanentemente (Hard Delete) |
>>>>>>> 2c93c95d113d71980f231decae191d1a1fdfd121
| `PATCH` | `/api/users/{id}/promote` | ✅ 👑 | Promove usuário a ADMIN (somente admins) |

---

## 🌿 Plantas (`/api/plants`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/plants` | ✅ | Cria uma nova planta |
<<<<<<< HEAD
| `GET` | `/api/plants` | ✅ | Lista todas as plantas do usuário (ativas) |
| `GET` | `/api/plants/{id}` | ✅ | Retorna detalhes de uma planta específica |
| `PUT` | `/api/plants/{id}` | ✅ | Atualiza informações de uma planta |
| `DELETE` | `/api/plants/{id}` | ✅ | Remove uma planta completamente |
| `PATCH` | `/api/plants/{id}/harvest` | ✅ | Realiza a colheita de uma planta (ex: frutos) |
| `PATCH` | `/api/plants/{id}/archive` | ✅ | Arquiva uma planta (Body opcional: notas/motivo em string pura) |
| `GET` | `/api/plants/history` | ✅ | Histórico paginado de plantas (Query params: `page` e `size`, padrão 0 e 10) |
| `GET` | `/api/plants/feed` | ✅ | Feed comunitário (lista plantas de todos os usuários) |
| `GET` | `/api/plants/species` | ✅ | Retorna lista de espécies disponíveis, seus nomes formatados e utilidade |
=======
| `GET` | `/api/plants` | ✅ | Lista todas as plantas ativas do usuário |
| `GET` | `/api/plants/{id}` | ✅ | Retorna uma planta por ID |
| `PUT` | `/api/plants/{id}` | ✅ | Atualiza uma planta |
| `PATCH` | `/api/plants/{id}/harvest` | ✅ | Marca uma planta como colhida |
| `PATCH` | `/api/plants/{id}/archive` | ✅ | Arquiva uma planta |
| `DELETE` | `/api/plants/{id}` | ✅ | Remove uma planta |
| `GET` | `/api/plants/history` | ✅ | Retorna o histórico paginado das plantas |
| `GET` | `/api/plants/feed` | ✅ | Feed comunitário (todas as plantas) |
>>>>>>> 2c93c95d113d71980f231decae191d1a1fdfd121

### Payload de Exemplos

**Criação de Planta (`POST /api/plants`):**
```json
{
  "name": "Minha Samambaia",
  "species": "SAMAMBAIA",
  "customSpeciesName": null,
  "soilType": "ARGILOSO"
}
```

<<<<<<< HEAD
**Arquivar Planta (`PATCH /api/plants/{id}/archive`):**
```json
"A planta secou durante o inverno."
```

**Histórico de Plantas (`GET /api/plants/history?page=0&size=10`):**
Retorna um objeto `Page` do Spring contendo `HistoryResponseDTO`.

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

## 💬 Feedback do App (`/api/feedback`)

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
=======
---

## 💡 Feedback do App (`/api/feedback`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/feedback` | ✅ | Submete um feedback ou sugestão para o aplicativo |
| `GET` | `/api/feedback` | ✅ | Lista todos os feedbacks (pode ter restrição de acesso futuramente) |
>>>>>>> 2c93c95d113d71980f231decae191d1a1fdfd121

---

## 📋 Planos de Cuidado (`/api/plants/{plantId}/cares`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/plants/{plantId}/cares` | ✅ | Cria um plano de cuidado para a planta |
| `GET` | `/api/plants/{plantId}/cares` | ✅ | Lista os planos de cuidado da planta |
| `PUT` | `/api/plants/{plantId}/cares/{careId}` | ✅ | Atualiza um plano de cuidado |
| `DELETE` | `/api/plants/{plantId}/cares/{careId}` | ✅ | Remove um plano de cuidado |
| `POST` | `/api/plants/{plantId}/cares/{careId}/complete`| ✅ | Conclui um cuidado (Body opcional: notas) |

### Payload de Exemplos

**Criação/Atualização (`POST` e `PUT`):**
```json
{
  "careType": "REGA",
  "frequencyType": "SEMANAL",
  "startDate": "2026-05-10"
}
```

**Concluir cuidado (`POST .../complete`):**
```json
"Reguei com 200ml de água."
```
> **Nota:** Marcar um cuidado como concluído atualiza automaticamente a data do próximo cuidado com base na frequência (`frequencyType`), registra no histórico e atualiza a gamificação do usuário (pontos/streak).

---

## ✅ Registros de Cuidado (`/api/cares/{carePlanId}/records`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/cares/{carePlanId}/records` | ✅ | Adiciona um registro manual de cuidado |
| `GET` | `/api/cares/{carePlanId}/records` | ✅ | Lista os registros históricos de um plano |

**Exemplo de Payload (`POST`):**
```json
{
  "notes": "Adicionei adubo NPK 10-10-10",
  "careDate": "2026-05-11"
}
```

---

## 📈 Observabilidade (`/actuator`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `GET` | `/actuator/health` | ❌ | Status de saúde da aplicação |
| `GET` | `/actuator/info` | ❌ | Informações da aplicação |
| `GET` | `/actuator/metrics` | ❌ | Métricas de performance |

---

## 📖 Documentação Interativa (Swagger)

A API fornece uma interface interativa:
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/api-docs`
