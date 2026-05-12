# Arquitetura e Integração Frontend — Grevia API

Bem-vindo à documentação mestre da **Grevia API**. Este arquivo (`ARCHITECTURE.md`) foi desenhado para ser o **guia definitivo para IAs de Frontend** e desenvolvedores que desejam se integrar ao sistema. Nele está consolidado *O Que a API Faz*, *Como Ela Funciona* e *Todos os Seus Endpoints*.

---

## 1. VISÃO GERAL
A Grevia API é um monólito em Spring Boot 3.5 com Java 21, construído em arquitetura de domínios modulares (`core`, `user`, `plant`, `care`). O sistema gerencia jardins virtuais, oferecendo **automação de cuidados (rega, poda)**, **gamificação** (streaks e pontos) e **histórico de plantas** (colheita e arquivamento).

O sistema é **Stateless**, autenticado via **JWT**, e expõe dados em formato **JSON**.

---

## 2. REGRAS DE INTEGRAÇÃO (Para IAs de Frontend)

- **CORS:** Liberado para `localhost:5173`, `localhost:3000` e domínios Vercel (em produção).
- **Autenticação:** Obrigatória (exceto nas rotas públicas). O cabeçalho deve ser `Authorization: Bearer <seu_token>`. Retornos `401` devem redirecionar para Login.
- **Tratamento de Rate Limit:** A API usa *Bucket4j* para bloqueio contra DDoS. Se receber um `429 Too Many Requests`, alerte o usuário para aguardar.
- **Gamificação Sincronizada:** Ao realizar a ação `POST /api/plants/{plantId}/cares/{careId}/complete`, os pontos e streak do usuário mudam no banco de dados. A UI deve atualizar esses valores (sugere-se refazer `GET /api/users/me` em seguida ou inferir atualizações otimistas).
- **Paginação:** Preste atenção na rota `GET /api/plants/history`, pois ela retorna um objeto encapsulado pelo Spring Data Page (`content`, `totalPages`, `totalElements`).

---

## 3. REFERÊNCIA DE ENDPOINTS COMPLETOS

### 3.1. Autenticação (`/api/auth`)
| Método | Endpoint | Protegido | Body | Descrição |
|---|---|---|---|---|
| `POST` | `/api/auth/register` | ❌ | `name`, `email`, `password` | Cria conta |
| `POST` | `/api/auth/login` | ❌ | `email`, `password` | Retorna `{ token: "..." }` |
| `POST` | `/api/auth/forgot-password` | ❌ | `email` | Dispara email de reset |
| `POST` | `/api/auth/reset-password` | ❌ | `token`, `newPassword` | Redefine a senha |

### 3.2. Usuários (`/api/users`)
| Método | Endpoint | Protegido | Body | Descrição |
|---|---|---|---|---|
| `GET` | `/api/users/me` | ✅ | - | Retorna perfil + streak + pontos |
| `PUT` | `/api/users/me` | ✅ | `name`, `email`, `password` | Atualiza perfil |
| `PUT` | `/api/users/me/password` | ✅ | `currentPassword`, `newPassword` | Altera a senha do usuário |
| `DELETE`| `/api/users/me` | ✅ | - | Exclui a conta permanentemente |
| `PATCH` | `/api/users/{id}/promote` | ✅ | - | Promove a ADMIN (só admins) |

### 3.3. Plantas (`/api/plants`)
| Método | Endpoint | Protegido | Body | Descrição |
|---|---|---|---|---|
| `POST` | `/api/plants` | ✅ | `name`, `species`, `soilType`, `customSpeciesName` (opcional) | Adiciona planta. Planos de cuidado baseados na espécie nascem automaticamente. |
| `GET` | `/api/plants` | ✅ | - | Lista todas as plantas ativas do usuário logado. |
| `GET` | `/api/plants/{id}` | ✅ | - | Dados e recomendações detalhadas da planta. |
| `PUT` | `/api/plants/{id}` | ✅ | Mesmo do POST | Atualiza a planta. |
| `DELETE`| `/api/plants/{id}` | ✅ | - | Remove fisicamente a planta do banco. |
| `PATCH` | `/api/plants/{id}/harvest` | ✅ | - | Marca a planta como colhida. |
| `PATCH` | `/api/plants/{id}/archive` | ✅ | *String* (Motivo/Nota) | Move a planta para o histórico (arquivada/morta). |
| `GET` | `/api/plants/history` | ✅ | `page=0&size=10` (Query) | Histórico de plantas (paginado). |
| `GET` | `/api/plants/feed` | ✅ | - | Lista as plantas recentes de toda a comunidade. |
| `GET` | `/api/plants/species` | ✅ | - | Retorna dicionário de Espécies (para montar `select/options`). |

### 3.4. Planos de Cuidado (`/api/plants/{plantId}/cares`)
| Método | Endpoint | Protegido | Body | Descrição |
|---|---|---|---|---|
| `POST` | `/.../cares` | ✅ | `careType`, `frequencyType`, `startDate` | Agenda um cuidado futuro. |
| `GET` | `/.../cares` | ✅ | - | Lista agendamentos da planta. |
| `PUT` | `/.../cares/{careId}` | ✅ | Mesmo do POST | Altera a rotina. |
| `DELETE`| `/.../cares/{careId}` | ✅ | - | Cancela o agendamento. |
| `POST` | `/.../cares/{careId}/complete` | ✅ | *String* (Opcional - nota) | **Mais importante:** Marca cuidado como feito, cria `CareRecord`, avança `nextCareDate` e dá pontos ao usuário. |

### 3.5. Histórico de Cuidado (`/api/cares/{carePlanId}/records`)
| Método | Endpoint | Protegido | Body | Descrição |
|---|---|---|---|---|
| `POST` | `/.../records` | ✅ | `notes`, `careDate` | Adiciona um registro isolado manual (se necessário). |
| `GET` | `/.../records` | ✅ | - | Lista as execuções de um plano. |

### 3.6. Feedback do App (`/api/feedback`)
| Método | Endpoint | Protegido | Body | Descrição |
|---|---|---|---|---|
| `POST` | `/api/feedback` | ✅ | `category`, `message` | Envia feedback sobre o aplicativo. |
| `GET` | `/api/feedback` | ✅ | - | Lista feedbacks (uso administrativo). |

---

## 4. LISTA DE ENUMERAÇÕES PARA FRONTEND

Envie essas strings EXATAS nos payloads do tipo JSON:

- **Espécies (`species`):** Use o endpoint `GET /api/plants/species` para popular o Dropdown. 
  *Ex: `SAMAMBAIA`, `ESPADA_DE_SAO_JORGE`, `TOMATE`*
- **Tipo de Solo (`soilType`):** `ARENOSO`, `ARGILOSO`, `HUMOSO`, `CALCARIO`, `MISTO`.
- **Tipo de Cuidado (`careType`):** `REGA`, `PODA`, `ADUBACAO`, `TRANSPLANTE`, `CONTROLE_PRAGAS`, `OUTRO`.
- **Frequência (`frequencyType`):** `DIARIO`, `SEMANAL`, `DUAS_VEZES_SEMANA`, `TRES_VEZES_SEMANA`, `QUINZENAL`, `MENSAL`, `BIMESTRAL`, `SOB_DEMANDA`.

---

## 5. EXEMPLO PRÁTICO DE INTEGRAÇÃO (FETCH API)

**Cenário:** Colher uma planta.
```javascript
const token = localStorage.getItem('token');
const plantId = 12;

fetch(`http://localhost:8080/api/plants/${plantId}/harvest`, {
  method: 'PATCH',
  headers: {
    'Authorization': `Bearer ${token}`
  }
}).then(res => {
  if (res.ok) {
    alert("Planta colhida com sucesso!");
    // Atualizar UI
  }
});
```

**Cenário:** Concluir a Rega da planta.
```javascript
fetch(`http://localhost:8080/api/plants/${plantId}/cares/${careId}/complete`, {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify("Usei 500ml de água, estava bem seca.")
}).then(async res => {
  if (res.ok) {
    const data = await res.json();
    console.log("Próxima rega será em:", data.nextCareDate);
    // Dispare um evento para recarregar a pontuação do Header do site (GET /api/users/me)
  }
});
```