# 📡 Referência de Endpoints — Grevia API

> Base URL: `http://localhost:8080` (dev) | `https://grevia-api-production.up.railway.app` (prod)

Todos os endpoints protegidos requerem o header:
```
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

### `POST /api/auth/register`

```json
// Request Body
{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senhaSegura123"
}
```

### `POST /api/auth/login`

```json
// Request Body
{
  "email": "joao@email.com",
  "password": "senhaSegura123"
}

// Response
{
  "token": "eyJhbGciOi..."
}
```

### `POST /api/auth/forgot-password`

```json
// Request Body
{
  "email": "joao@email.com"
}
```

### `POST /api/auth/reset-password`

```json
// Request Body
{
  "token": "abc123-token-recebido-por-email",
  "newPassword": "novaSenha456"
}
```

---

## 👤 Usuários (`/api/users`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `GET` | `/api/users/me` | ✅ | Retorna o perfil do usuário autenticado |
| `PUT` | `/api/users/me` | ✅ | Atualiza o perfil do usuário autenticado |
| `PUT` | `/api/users/me/password` | ✅ | Altera a senha do usuário autenticado |
| `DELETE` | `/api/users/me` | ✅ | Exclui a conta do usuário permanentemente (Hard Delete) |
| `PATCH` | `/api/users/{id}/promote` | ✅ 👑 | Promove usuário a ADMIN (somente admins) |

---

## 🌿 Plantas (`/api/plants`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/plants` | ✅ | Cria uma nova planta |
| `GET` | `/api/plants` | ✅ | Lista todas as plantas ativas do usuário |
| `GET` | `/api/plants/{id}` | ✅ | Retorna uma planta por ID |
| `PUT` | `/api/plants/{id}` | ✅ | Atualiza uma planta |
| `PATCH` | `/api/plants/{id}/harvest` | ✅ | Marca uma planta como colhida |
| `PATCH` | `/api/plants/{id}/archive` | ✅ | Arquiva uma planta |
| `DELETE` | `/api/plants/{id}` | ✅ | Remove uma planta |
| `GET` | `/api/plants/history` | ✅ | Retorna o histórico paginado das plantas |
| `GET` | `/api/plants/feed` | ✅ | Feed comunitário (todas as plantas) |

### `POST /api/plants`

```json
// Request Body
{
  "name": "Minha Samambaia",
  "species": "SAMAMBAIA",
  "plantType": "ORNAMENTAL",
  "terrainType": "ARGILOSO"
}
```

---

## 💡 Feedback do App (`/api/feedback`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/feedback` | ✅ | Submete um feedback ou sugestão para o aplicativo |
| `GET` | `/api/feedback` | ✅ | Lista todos os feedbacks (pode ter restrição de acesso futuramente) |

---

## 📋 Planos de Cuidado (`/api/plants/{plantId}/cares`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/plants/{plantId}/cares` | ✅ | Cria um plano de cuidado para a planta |
| `GET` | `/api/plants/{plantId}/cares` | ✅ | Lista os planos de cuidado da planta |
| `PUT` | `/api/plants/{plantId}/cares/{careId}` | ✅ | Atualiza um plano de cuidado |
| `DELETE` | `/api/plants/{plantId}/cares/{careId}` | ✅ | Remove um plano de cuidado |

---

## ✅ Registros de Cuidado (`/api/cares/{carePlanId}/records`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `POST` | `/api/cares/{carePlanId}/records` | ✅ | Registra um cuidado realizado |
| `GET` | `/api/cares/{carePlanId}/records` | ✅ | Lista os registros do plano de cuidado |

---

## 📈 Observabilidade (`/actuator`)

| Método | Rota | Protegido | Descrição |
|---|---|---|---|
| `GET` | `/actuator/health` | ❌ | Status de saúde da aplicação |
| `GET` | `/actuator/info` | ❌ | Informações da aplicação |
| `GET` | `/actuator/metrics` | ❌ | Métricas de performance |

---

## 📖 Documentação Interativa (Swagger)

Acesse a documentação interativa completa (gerada automaticamente pelo Springdoc OpenAPI):

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)
