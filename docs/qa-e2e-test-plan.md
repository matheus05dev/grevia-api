# Plano de Testes QA (End-to-End via Frontend) — Grevia App

Este documento descreve os cenários de testes End-to-End (E2E) com foco na **Interface do Usuário (Frontend)**, ideal para automação com ferramentas como **Cypress** ou **Playwright**. Ele mapeia as interações do usuário no navegador e as respostas esperadas da API no background.

> **Base URL Frontend:** `http://localhost:3000`
> **Base URL Backend (API):** `http://localhost:8080/api`

---

## 🧑‍💻 Fluxo 1: Autenticação e Gestão de Conta (Identity Flow)

**Objetivo:** Garantir que o bot navegue pela interface de cadastro, login e consiga editar ou deletar sua conta pelas páginas da aplicação.

| Passo | Ação na Interface (UI) | Interceptação de Rede (Network) | Assertiva / Validação (UI) |
|---|---|---|---|
| **1.1 Acessar Cadastro** | Navegar para `http://localhost:3000/register` | - | O formulário de registro deve estar visível. |
| **1.2 Preencher Cadastro** | Digitar nome, e-mail (ex: `bot@qa.com`) e senha, e clicar em "Cadastrar" | Espionar `POST /api/auth/register` (Esperar status `200/201`) | Redirecionamento para a página de Login com mensagem de sucesso. |
| **1.3 Login** | Em `/login`, preencher e-mail e senha, e clicar em "Entrar" | Espionar `POST /api/auth/login` (Esperar status `200` e token) | Redirecionamento para o `/dashboard`. Header de saudação visível. |
| **1.4 Meu Perfil** | Clicar no menu de usuário e ir para "Meu Perfil" | Espionar `GET /api/users/me` | Dados do usuário (`bot@qa.com`) devem aparecer nos campos. |
| **1.5 Edição de Dados** | Mudar o nome no input e clicar em "Salvar" | Espionar `PUT /api/users/me` | Toast/Notificação de "Perfil atualizado com sucesso". |
| **1.6 Exclusão da Conta** | Clicar no botão "Excluir Conta" (e confirmar no modal) | Espionar `DELETE /api/users/me` (Esperar `204`) | Redirecionamento forçado para a tela de `/login` ou Home pública. |
| **1.7 Validar Exclusão** | Tentar logar novamente com os dados antigos | Espionar `POST /api/auth/login` (Esperar `401` ou `404`) | Mensagem de erro de credenciais inválidas na tela. |

---

## 🌱 Fluxo 2: Ciclo de Vida da Planta (Plant Lifecycle Flow)

**Pré-requisito:** Estar logado e no `/dashboard`.

**Objetivo:** Testar se o usuário consegue adicionar uma planta na UI, visualizá-la no painel, colher/arquivar e apagar.

| Passo | Ação na Interface (UI) | Interceptação de Rede (Network) | Assertiva / Validação (UI) |
|---|---|---|---|
| **2.1 Modal de Planta** | Clicar no botão "Adicionar Planta" ou "Nova Planta" | - | Modal/Formulário de cadastro se abre. |
| **2.2 Preencher Planta** | Selecionar "Espécie", "Tipo de Terreno", digitar "Nome" e clicar em "Salvar" | Espionar `POST /api/plants` (Esperar `200/201`) | Modal fecha. O card da nova planta deve aparecer no Dashboard. |
| **2.3 Feed Público** | Navegar para a página de Feed (ex: `/feed`) | Espionar `GET /api/plants/feed` | O card da planta criada deve estar visível na lista pública. |
| **2.4 Histórico** | Ir para o painel de Histórico/Plantas Antigas (`/history`) | Espionar `GET /api/plants/history` | O histórico de plantas abre corretamente (pode estar vazio inicialmente). |
| **2.5 Colheita/Arquivo** | No card da planta (no Dashboard), clicar no botão de "Colher" ou "Arquivar" | Espionar `PATCH /api/plants/{id}/harvest` (ou `/archive`) | O card some do Dashboard principal e passa a aparecer na aba de Histórico. |
| **2.6 Deleção** | Na aba de Histórico, clicar em "Excluir Definitivamente" no card | Espionar `DELETE /api/plants/{id}` | O card desaparece permanentemente da UI. |

---

## 📋 Fluxo 3: Gestão de Cuidados (Care Flow)

**Pré-requisito:** Ter pelo menos 1 planta ativa no Dashboard.

**Objetivo:** Testar a adição de rotinas de cuidado pela interface e o preenchimento de que a ação foi realizada.

| Passo | Ação na Interface (UI) | Interceptação de Rede (Network) | Assertiva / Validação (UI) |
|---|---|---|---|
| **3.1 Acessar Cuidados** | Clicar no card da planta e abrir a aba/página de "Cuidados" | Espionar `GET /api/plants/{id}/cares` | Título da planta e área vazia de cuidados são exibidos. |
| **3.2 Criar Rotina** | Clicar em "Novo Cuidado", selecionar "Rega", frequência "Semanal", e "Salvar" | Espionar `POST /api/plants/{id}/cares` | O novo plano de rega aparece na lista de rotinas ativas da planta. |
| **3.3 Check-in (Registrar)** | Clicar no botão "Feito" ou "Regar Agora" ao lado do card da rotina | Espionar `POST /api/cares/{carePlanId}/records` | O botão muda de estado (ex: "Realizado"). |
| **3.4 Histórico de Cuidados** | Abrir a aba de "Histórico de Cuidados" | Espionar `GET /api/cares/{carePlanId}/records` | O registro da rega feita aparece na linha do tempo. |

---

## 💡 Fluxo 4: Feedback e Sugestões (Feedback App)

**Pré-requisito:** Estar logado.

**Objetivo:** Interagir com o widget ou página de Feedback.

| Passo | Ação na Interface (UI) | Interceptação de Rede (Network) | Assertiva / Validação (UI) |
|---|---|---|---|
| **4.1 Abrir Feedback** | Clicar no botão "Dar Feedback" ou acessar `/feedback` | - | Modal/Formulário de envio aparece. |
| **4.2 Preencher e Enviar**| Selecionar categoria, digitar sugestão e "Enviar" | Espionar `POST /api/feedback` (Esperar `200`) | Modal se fecha e exibe notificação: "Feedback enviado com sucesso! Obrigado". |

---

## 🛠️ Dicas para o Bot de UI (Cypress / Playwright)

1. **Atributos `data-cy` ou `data-testid`:**
   Garanta que no código do Frontend (na porta 3000), os elementos interativos tenham atributos identificadores exclusivos (ex: `<button data-testid="submit-login">`). Isso torna o bot muito mais robusto e imune a quebras por mudanças visuais (CSS/classes).

2. **Interceptação de API (`cy.intercept()` / `page.route()`):**
   Não dependa apenas de um toast para saber se salvou. Intercepte a requisição de rede (`http://localhost:8080/api/*`) e aguarde que o servidor responda `200` ou `201` antes de tentar localizar o elemento novo na tela.

3. **Login Programático (Bypass de UI):**
   Para testar Fluxos 2, 3 e 4, você não precisa fazer o bot digitar a senha na tela de login todas as vezes. Faça uma requisição HTTP via código do bot (`cy.request`), pegue o JWT, injete direto no `localStorage` do navegador e acesse o `localhost:3000/dashboard`. Isso deixa a suíte E2E muito mais rápida.
